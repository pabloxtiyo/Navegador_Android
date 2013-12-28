package com.uesocc.app;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by xtiyo on 12-24-13.
 */
public class ConfiguracionActivity extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.xml.configuraciones);
    }
}
