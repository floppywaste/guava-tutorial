package com.github.floppywaste;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.base.Preconditions;


public class Guava01_Preconditions {

    @Test
    public void checkState_preconditionsMakeYourCodeMoreConcise() throws Exception {
        boolean iWillNotFail = true;
        String arg1 = "waste";
        String arg2 = "characters";

        if (!iWillNotFail) {
            throw new IllegalStateException("This is certainly a " + arg1 + " of " + arg2 + ".");
        }

        Preconditions.checkState(iWillNotFail, "We don't wanna %s too much %s actually.", arg1, arg2);
    }

    @Test
    public void checkNotNull_canBeUsedInline() throws Exception {
        assertThat(Preconditions.checkNotNull("i will pass")).isEqualTo("i will pass");

        assertThat(checkNotNull("using static import makes it more readable")).isEqualTo(
                "using static import makes it more readable");
    }

    @SuppressWarnings("unused")
    @Test(expected = NullPointerException.class)
    public void checkNotNull_exitsProgramFlowWhereYouWantItTo() throws Exception {
        Preconditions.checkNotNull(null, "idiot!");

        Assert.fail("must not be reached");
    }

}
