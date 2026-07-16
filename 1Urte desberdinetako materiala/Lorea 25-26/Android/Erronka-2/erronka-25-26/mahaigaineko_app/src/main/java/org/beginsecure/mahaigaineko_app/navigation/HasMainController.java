package org.beginsecure.mahaigaineko_app.navigation;

import org.beginsecure.mahaigaineko_app.controller.layout.MainLayoutController;

public interface HasMainController {
    /**
     * Controller honek MainLayoutController behar duenean, hemen jasotzen du nabigaziorako.
     */
    void setMainController(MainLayoutController mainController);
}