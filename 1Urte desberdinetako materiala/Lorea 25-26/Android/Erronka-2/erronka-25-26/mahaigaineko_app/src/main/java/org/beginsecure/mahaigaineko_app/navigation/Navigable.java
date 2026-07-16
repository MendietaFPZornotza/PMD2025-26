package org.beginsecure.mahaigaineko_app.navigation;

import java.util.Map;

public interface Navigable {
    /**
     * Bista batera nabigatzean parametroak jaso nahi badira, metodo hau inplementatu.
     */
    default void onNavigate(Map<String, Object> params) {}
}