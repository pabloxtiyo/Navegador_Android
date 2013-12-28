package com.uesocc.app;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.webkit.WebChromeClient;
import android.webkit.WebIconDatabase;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends ActionBarActivity implements View.OnClickListener{

    private Button parar,adelante,atras,ir,borrar,historial;
    private ImageView fav;
    private ProgressBar progreso;
    private EditText url;
    private WebView webview;
    private int error=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        parar = (Button) findViewById(R.id.Parar);
        adelante = (Button) findViewById(R.id.Adelante);
        atras = (Button)    findViewById(R.id.Atras);
        ir = (Button) findViewById(R.id.Ir);
        borrar = (Button) findViewById(R.id.BorrarHistorial);
        historial = (Button) findViewById(R.id.Historial);
        fav = (ImageView) findViewById(R.id.Favicon);
        url = (EditText) findViewById(R.id.url);
        progreso = (ProgressBar) findViewById(R.id.progressBar);
        webview = (WebView) findViewById(R.id.webView);
//Soporte para evento click de los botones

        parar.setOnClickListener(this);
        adelante.setOnClickListener(this);
        atras.setOnClickListener(this);
        ir.setOnClickListener(this);
        borrar.setOnClickListener(this);
        historial.setOnClickListener(this);
        //Pagina de Inicio
        webview.loadUrl("http://www.debian.org");

        //soporte para JS
        webview.getSettings().setJavaScriptEnabled(true);

        //Agregar soporte para Zoom
        webview.getSettings().setBuiltInZoomControls(true);
        webview.getSettings().setSupportZoom(true);

        //Soporte favicon
        WebIconDatabase.getInstance().open(getDir("icons", MODE_PRIVATE).getPath());

        webview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {


                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_UP:
                        if (!v.hasFocus()) {
                            v.requestFocus();
                        }

                        break;
                    //case MotionEvent.ACTION_
                }

                return false;
            }
        });
        webview.setWebChromeClient(new WebChromeClient(){

            @Override
            public void onReceivedIcon(WebView view, Bitmap icon){
                fav.setImageBitmap(icon);

            }

            @Override
            public void onReceivedTitle (WebView view, String title){
                getWindow().setTitle("xtiyo on "+title);
            }

        });

        webview.setWebViewClient(new WebViewClient(){

            @Override
            public void onPageStarted (WebView view, String url, Bitmap favicon){
                progreso.setVisibility(View.VISIBLE);
                // info.setText("Conectando!........");
            }

            @Override
            public void onPageFinished(WebView view, String url){
                progreso.setVisibility(View.INVISIBLE);

                if(error==0){
                Calendar c = Calendar.getInstance();
                SimpleDateFormat df1 = new SimpleDateFormat("dd-MMM-yyyy");
                SimpleDateFormat df2 = new SimpleDateFormat("HH:mm:ss");
                String fecha = df1.format(c.getTime());
                String hora = df2.format(c.getTime());
                escribir(url,fecha,hora);
                }
                error=0;
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage(description).setPositiveButton("Aceptar", null).setTitle("Error! web page "+failingUrl);
                builder.show();
                error=1;
            }

            });

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==ir.getId()){
            String page = url.getText().toString();
            webview.loadUrl(page);
        }
        if(v.getId()==parar.getId()){
            webview.stopLoading();
        }
        if(v.getId()==adelante.getId()){
            if(webview.canGoForward()){
                webview.goForward();
            }else{

                Toast.makeText(this,"No existen paginas q visitar",Toast.LENGTH_LONG).show();
            }
        }
        if(v.getId()==atras.getId()){
            if(webview.canGoBack()){
                webview.goBack();
            }else{

                Toast.makeText(this,"No existen paginas q visitar",Toast.LENGTH_LONG).show();
            }
        }
        if(v.getId()==historial.getId()){
            Intent j = new Intent(this,HistorialActivity.class);
            startActivity(j);
        }
        if (v.getId()==borrar.getId()){

       // borrar();
            Intent j = new Intent(this,ConfiguracionActivity.class);
           startActivity(j);


        }
    }

    public void escribir(String url,String hora,String fecha){

        SQLHelper helper = new SQLHelper(this,"HISTORIAL",null,1);
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("INSERT INTO info values('"+url+"','"+fecha+"','"+hora+"')");
        db.close();
        helper.close();
    }

    public  void borrar(){
        SQLHelper helper = new SQLHelper(this,"HISTORIAL",null,1);
        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete("info",null,null);

    }
    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }

}
