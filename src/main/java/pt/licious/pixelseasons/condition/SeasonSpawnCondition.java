package pt.licious.pixelseasons.condition;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.pixelmonmod.pixelmon.api.spawning.SpawnInfo;
import com.pixelmonmod.pixelmon.api.spawning.SpawnLocation;
import com.pixelmonmod.pixelmon.api.spawning.conditions.SpawnCondition;
import net.minecraft.world.World;
import sereneseasons.api.season.ISeasonState;
import sereneseasons.api.season.Season;
import sereneseasons.api.season.SeasonHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.Set;

public class SeasonSpawnCondition extends SpawnCondition {

    private static final transient Season[] SEASONS = Season.values();
    private static final transient Season.SubSeason[] SUB_SEASONS = Season.SubSeason.values();
    private static final transient Season.TropicalSeason[] TROPICAL_SEASONS = Season.TropicalSeason.values();

    private Set<String> seasons = null;
    private Set<String> subSeasons = null;
    private Set<String> tropicalSeasons = null;
    private transient final Set<Season> cachedSeasons = EnumSet.noneOf(Season.class);
    private transient final Set<Season.SubSeason> cachedSubSeasons = EnumSet.noneOf(Season.SubSeason.class);
    private transient final Set<Season.TropicalSeason> cachedTropicalSeasons = EnumSet.noneOf(Season.TropicalSeason.class);

    @Override
    public void onExport() {
        super.onExport();
        // Wouldn't it be nice if we had the ability to implement our own adapter?
        this.seasons = this.cachedSeasons.isEmpty() ? null : Sets.newHashSet();
        if (this.seasons != null)
            this.exportEnums(this.cachedSeasons, this.seasons);
        this.subSeasons = this.cachedSubSeasons.isEmpty() ? null : Sets.newHashSet();
        if (this.subSeasons != null)
            this.exportEnums(this.cachedSubSeasons, this.subSeasons);
        this.tropicalSeasons = this.cachedTropicalSeasons.isEmpty() ? null : Sets.newHashSet();
        if (this.tropicalSeasons != null)
            this.exportEnums(this.cachedTropicalSeasons, this.tropicalSeasons);
    }

    @Override
    public void onImport() {
        super.onImport();
        this.importEnums(this.seasons, this.cachedSeasons, SEASONS);
        this.importEnums(this.subSeasons, this.cachedSubSeasons, SUB_SEASONS);
        this.importEnums(this.tropicalSeasons, this.cachedTropicalSeasons, TROPICAL_SEASONS);
    }

    @Override
    public boolean fits(SpawnInfo spawnInfo, SpawnLocation spawnLocation) {
        final boolean baseResult = super.fits(spawnInfo, spawnLocation);
        // No point in going on.
        if (!baseResult)
            return false;
        final World world = spawnLocation.location.world;
        // You never know.
        if (world == null)
            return false;
        final ISeasonState seasonState = SeasonHelper.getSeasonState(world);
        final boolean seasonResult = this.checkState(this.cachedSeasons, seasonState.getSeason());
        final boolean subSeasonResult = this.checkState(this.cachedSubSeasons, seasonState.getSubSeason());
        final boolean tropicalSeasonResult = this.checkState(this.cachedTropicalSeasons, seasonState.getTropicalSeason());
        return seasonResult && subSeasonResult && tropicalSeasonResult;
    }

    /**
     * Returns an immutable set containing the required {@link Season} if any.
     *
     * @return the required {@link Season}'s.
     */
    @Nonnull
    public ImmutableSet<Season> seasons() {
        return Sets.immutableEnumSet(this.cachedSeasons);
    }

    /**
     * Returns an immutable set containing the required {@link Season.SubSeason} if any.
     *
     * @return the required {@link Season.SubSeason}'s.
     */
    @Nonnull
    public ImmutableSet<Season.SubSeason> subSeasons() {
        return Sets.immutableEnumSet(this.cachedSubSeasons);
    }

    /**
     * Returns an immutable set containing the required {@link Season.TropicalSeason} if any.
     *
     * @return the required {@link Season.TropicalSeason}'s.
     */
    @Nonnull
    public ImmutableSet<Season.TropicalSeason> tropicalSeasons() {
        return Sets.immutableEnumSet(this.cachedTropicalSeasons);
    }

    private <E extends Enum<E>> void exportEnums(Set<E> holder, Set<String> output) {
        for (E value : holder) {
            output.add(value.name());
        }
    }

    private <E extends Enum<E>> void importEnums(@Nullable Set<String> inputs, Set<E> holder, E[] values) {
        holder.clear();
        if (inputs == null)
            return;
        for (String input : inputs) {
            for (E value : values) {
                if (value.name().equalsIgnoreCase(input)) {
                    holder.add(value);
                    break;
                }
            }
        }
    }

    private <E extends Enum<E>> boolean checkState(Set<E> holder, E current) {
        if (holder.isEmpty())
            return true;
        return holder.contains(current);
    }

}
