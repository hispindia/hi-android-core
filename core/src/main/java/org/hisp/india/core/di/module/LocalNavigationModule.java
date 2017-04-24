package org.hisp.india.core.di.module;

import org.hisp.india.core.common.LocalCiceroneHolder;
import org.hisp.india.core.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by nhancao on 4/20/17.
 */

@Module
public class LocalNavigationModule {

    @Provides
    @ActivityScope
    LocalCiceroneHolder provideLocalNavigationHolder() {
        return new LocalCiceroneHolder();
    }
}
