package com.iStudy.Study.ActionBarSherlock.Internal.View;

import com.iStudy.Study.ActionBarSherlock.Internal.View.Menu.SubMenuWrapper;
import com.iStudy.Study.ActionBarSherlock.View.ActionProvider;

import android.view.View;

public class ActionProviderWrapper extends android.view.ActionProvider {
    private final ActionProvider mProvider;


    public ActionProviderWrapper(ActionProvider provider) {
        super(null/*TODO*/); //XXX this *should* be unused
        mProvider = provider;
    }


    public ActionProvider unwrap() {
        return mProvider;
    }

    @Override
    public View onCreateActionView() {
        return mProvider.onCreateActionView();
    }

    @Override
    public boolean hasSubMenu() {
        return mProvider.hasSubMenu();
    }

    @Override
    public boolean onPerformDefaultAction() {
        return mProvider.onPerformDefaultAction();
    }

    @Override
    public void onPrepareSubMenu(android.view.SubMenu subMenu) {
        mProvider.onPrepareSubMenu(new SubMenuWrapper(subMenu));
    }
}
