package sanjuan.dhhp;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.EmptyStackException;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class SaveActy extends AppCompatActivity {

    private static final int STORAGE_PERMISSION_RC = 1;
    String proyecto;
    String zona; //de emplazamiento
    String observaciones;
    private Bundle extras;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_acty);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        extras = getIntent().getExtras();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        intro("", "", "");
    }
    private void intro(String name, String zona, String observaciones) {
        LinearLayout cV = (LinearLayout) getLayoutInflater().inflate(R.layout.intro_name, null);
//        final EditText luzE = cV.findViewById(R.id.zoomIn);
//        luzE.setText(luz);
        final EditText nameE = cV.findViewById(R.id.nombre);
        nameE.setText(name);
        final EditText zonaE = cV.findViewById(R.id.zona);
        zonaE.setText(zona);
        final EditText obsE = cV.findViewById(R.id.obs);
        obsE.setText(observaciones);

        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle(getString(R.string.intro_data));
        LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        ll.setMargins(10, 10, 10, 10);
        LinearLayout l = new LinearLayout(this);
        l.setLayoutParams(ll);
        l.setOrientation(LinearLayout.VERTICAL);
        cV.setLayoutParams(ll);
        ScrollView sc = new ScrollView(this);
        sc.setLayoutParams(ll);
        sc.addView(cV);
        b.setView(l);
        final DialogInterface.OnClickListener OkClick = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String error = "";
                boolean full = true;
//                double luzD = 0;
                String nameS = "";
                String zonaS = "";
                String obsS = "";
                try {
                    for (EditText et : new EditText[]{
//                            luzE,
                            nameE, zonaE, obsE}) {
                        if (et.getText().toString().isEmpty())
                            throw new EmptyStackException();
                    }
//                    luzD = Double.parseDouble(luzE.getText().toString());
                    nameS = nameE.getText().toString();
                    zonaS = zonaE.getText().toString();
                    obsS = obsE.getText().toString();
                } catch (EmptyStackException e) {
                    error = getString(R.string.err_fill_data_first);
                    full = false;
                }
                if (!full) {
                    Toast.makeText(SaveActy.this, error, Toast.LENGTH_SHORT).show();
                    intro(nameE.getText().toString(), zonaE.getText().toString(), obsE.getText().toString());
                } else {
                    setData(nameS, zonaS, obsS);
                }
            }
        };
        b.setPositiveButton(R.string.insertar, OkClick);
        l.addView(sc);
//        b.setOnDismissListener(new DialogInterface.OnDismissListener() {
//            @Override
//            public void onDismiss(DialogInterface dialog) {
//                OkClick.onClick(dialog, 0);
//            }
//        });
        b.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                OkClick.onClick(dialog, 0);
            }
        });
        b.show();
    }

    private void setData(String nameS, String zonaS, String obsS) {
        proyecto = nameS;
        zona = zonaS;
        observaciones = obsS;

        if (extras == null)
            extras = new Bundle();
        extras.putString(Calculator.PROYECTO, proyecto);
        extras.putString(Calculator.ZONA, zona);
        extras.putString(Calculator.OBSERVACIONES, obsS);

        ((TextView)findViewById(R.id.name)).setText(getString(R.string.nombre_del_proyecto)+proyecto);
        ((TextView)findViewById(R.id.zona)).setText(getString(R.string.zona_de_emplazamiento)+zona);
        ((TextView)findViewById(R.id.obs)).setText(getString(R.string.observaciones)+obsS);

        String all = "";
        all += getString(R.string.pendiente_m_m)+ extras.getDouble(Calculator.PENDIENTE)+"\n";
        all += getString(R.string.longitud_del_r_o_m)+ extras.getDouble(Calculator.LONGITUD)+"\n";
        all += getString(R.string.rea_de_la_cuenca_km_2)+ extras.getDouble(Calculator.AREA)+"\n";
//        all += getString(R.string.hp_mm)+ extras.getDouble(Calculator.HP)+"\n";
        all += getString(R.string.factor_seg_n_la_probabilidad_de_dise_o)+ extras.getDouble(Calculator.FACTOR_PROB)+"\n";
        all += getString(R.string.nivel_de_crecida_de_dise_o)+ extras.getDouble(Calculator.NIVEL_CRECIDA)+"\n";
        all += getString(R.string.tiempo_de_concentraci_n_1s,  extras.getDouble(Calculator.TC), extras.getDouble(Calculator.HP))+"\n";
        all += getString(R.string.intensidad_mm_min)+ extras.getDouble(Calculator.INTENSIDAD)+"\n";
        all += getString(R.string.gasto_acumulado)+ extras.getDouble(Calculator.GASTO_ACUMULADO)+"\n";
        all += getString(R.string.area_acumulada)+ extras.getDouble(Calculator.AREA_ACUMULADA)+"\n";
        all += getString(R.string.prof_max)+ extras.getDouble(Calculator.PROF_MAX)+"\n";
        all += getString(R.string.gasto_cause_principal)+ extras.getDouble(Calculator.GASTO_CAUSE_PRINCIPAL)+"\n";
        all += getString(R.string.sections)+ extras.getInt(Calculator.CANT_SECCIONES)+"\n";
        Bundle b = extras.getBundle(Calculator.SECCIONES);
        int m =  extras.getInt(Calculator.CANT_SECCIONES);
        try {
            for (int i = 0; i < m; i++) {
                Seccion s = ((Seccion) b.getSerializable(String.valueOf(i)));
                all += getString(R.string.section, String.valueOf(i)) + s.toString() + "\n";
            }
        }catch (Exception ignored){}

        all += getString(R.string.estribos, extras.getDouble(Calculator.ESTRIBO_INICIO),extras.getDouble(Calculator.ESTRIBO_FINAL))+"\n";
        all += getString(R.string.area_estri)+ extras.getDouble(Calculator.AREA_ESTRIBO)+"\n";
        all += getString(R.string.miu)+ extras.getDouble(Calculator.MIU)+"\n";
        all += getString(R.string.espesor_de_la_losa)+ extras.getDouble(Calculator.ESPESOR_LOSA)+"\n";
        all += getString(R.string.espesor_de_la_viga)+ extras.getDouble(Calculator.ESPESOR_VIGA)+"\n";
        all += getString(R.string.espesor_del_pavimento)+ extras.getDouble(Calculator.ESPESOR_PAVIMENTO)+"\n";
        all += getString(R.string.longitud)+ extras.getDouble(Calculator.LONG_ESTRIBO)+"\n";

        ((TextView)findViewById(R.id.all)).setText(all);
    }

    public void savetxt(View view) {
        if (needPermisions()) return;

        save();
    }

    private void save() {
        String t = "";
        t+= ((TextView)findViewById(R.id.name)).getText();
        t+= ((TextView)findViewById(R.id.zona)).getText();
        t+= ((TextView)findViewById(R.id.obs)).getText();
        t += (String) ((TextView)findViewById(R.id.all)).getText();

        String name = proyecto + "_" + Calendar.getInstance().getTime().toString();
        try {
            Calculator.writeToFile(name, t, this);
            Toast.makeText(this,
                    "Archivo guardado en /sdacard/ "+getString(R.string.app_name) +"/"+ name + ".txt",
                    Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
    }

    private boolean needPermisions() {
        if (ActivityCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                ){
            ActivityCompat.requestPermissions(this, new String[]{READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_RC);
            return true;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_RC) {
            for (int i : grantResults) {
                if (i!= PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "No se han concedido todos los permisos", Toast.LENGTH_SHORT).show();
                    needPermisions();
                }
            }

        }
    }

}
