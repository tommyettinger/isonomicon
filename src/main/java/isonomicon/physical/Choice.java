package isonomicon.physical;

@FunctionalInterface
public interface Choice {
    boolean choose(int x, int y, int z);
}
