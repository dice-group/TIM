package tim.iterative;

import tim.IMatcher;
import tim.MappingState;
import tim.Matcher;
import tim.util.ConsoleColors;

public abstract class IterativeMatcher extends Matcher implements IIterativeMatcher {

    //This is only used if it is not used interactively so not in an iterative combiner
    @Override
    public void run() {
        this.runIteration();
    }

    @Override
    public void init(IMatcher parent, MappingState state) {
        super.init(parent, state);
        System.out.println(ConsoleColors.PURPLE_BOLD_BRIGHT + getClass().getSimpleName() + "INIT " + ConsoleColors.RESET);
    }

    @Override
    public void reset() {
        super.reset();
        System.out.println(ConsoleColors.PURPLE_BOLD_BRIGHT + getClass().getSimpleName() + "RESET " + ConsoleColors.RESET);
    }

}
