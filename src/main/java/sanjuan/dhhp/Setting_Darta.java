package sanjuan.dhhp;

import android.app.Fragment;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.EmptyStackException;

import static sanjuan.dhhp.Utils.format;

public class Setting_Darta extends AppCompatActivity {

    private static final int FOR_RESULT_INTENSIDAD = 1;
    private double area;
    private double longitud;
    private double pendiente;
//    private double coeficiente;
    private double hp;
    private double factor_prob;
//    private double nivel_crecida;
    private double intensidad;
    private double tc;
    private Spinner categorias;
    private TextView caudalView;
//    private int cant_secciones;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_data);
        categorias = (Spinner) findViewById(R.id.categoria);
        categorias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0: // autopista
                        factor_prob = 1;
                        break;
                    case 1: // I - II
                        factor_prob = 0.83;
                        break;
                    case 2: // III - IV
                        factor_prob = 0.55;
                        break;
                }
                setCadudal();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        categorias.setSelection(0);

    }

    public void exit(View view) {
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (RESULT_OK == resultCode){
            switch (requestCode){
                case FOR_RESULT_INTENSIDAD:
                    intensidad = data.getDoubleExtra(Calculator.INTENSIDAD, 0l);
                    ((EditText)findViewById(R.id.intensidad)).setText(intensidad+"");
                    break;
            }
        }
        setCadudal();
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setCadudal() {
        checkData(new int[]{
                R.id.area, R.id.longitud, R.id.pendiente,
//                R.id.cant_secciones,
                R.id.hp,
//                R.id.factor_prob,
//                R.id.nivel_crecida,
//                R.id.intensidad
        });
        double coeficiente = Calculator.coeficienteEscurrimiento(pendiente);
        Double caudalDisenno = Calculator.caudalDisenno(area, coeficiente, intensidad, factor_prob);
        double RangMin = caudalDisenno * 0.95;
        double RangMax = caudalDisenno * 1.05;

        caudalView = (TextView) findViewById(R.id.caudal);
        caudalView.setText(getString(R.string.caudal_de_dise_o_1_0_f, format(caudalDisenno), format(RangMin), format(RangMax)));
    }

    public void finish(View view) {
        if (checkData(null)){
            Intent i = new Intent(this, MainActivity.class);
            i.putExtra(Calculator.PENDIENTE, pendiente);
//            i.putExtra(Calculator.COEFICIENTE, coeficiente);
            i.putExtra(Calculator.LONGITUD, longitud);
            i.putExtra(Calculator.AREA, area);
            i.putExtra(Calculator.HP, hp);
            i.putExtra(Calculator.FACTOR_PROB, factor_prob);
            i.putExtra(Calculator.TC, tc);
            i.putExtra(Calculator.INTENSIDAD, intensidad);
//            i.putExtra(Calculator.NIVEL_CRECIDA, nivel_crecida);
//            i.putExtra(Calculator.CANT_SECCIONES, cant_secciones);
            startActivity(i);
//            finish();
        }
    }

    private boolean checkData(int where[]) {
        int need[] = {
                R.id.area, R.id.longitud, R.id.pendiente,
//                R.id.cant_secciones,
                R.id.hp,
//                R.id.factor_prob,
//                R.id.nivel_crecida,
                R.id.intensidad};
        boolean full = true;
        int b = 0;
        String error = "";
        for (int i :where==null?need:where) {
            try{
                String t = ((EditText) findViewById(i)).getText().toString();
                if (t.isEmpty()){
                    throw new EmptyStackException();
                }
                switch (i){
                    case R.id.area:
                        area = Double.parseDouble(t);
                        if (area>30)
                            throw new IndexOutOfBoundsException();
                        break;
                    case R.id.longitud:
                        longitud = Double.parseDouble(t);
                        break;
                    case R.id.pendiente:
                        pendiente = Double.parseDouble(t);
                        break;
//                    case R.id.cant_secciones:
//                        cant_secciones = Integer.parseInt(t);
//                        break;
                    case R.id.hp:
                        hp = Double.parseDouble(t);
                        break;
//                    case R.id.factor_prob:
//                        factor_prob= Double.parseDouble(t);
//                        break;
//                    case R.id.nivel_crecida:
//                        nivel_crecida= Double.parseDouble(t);
//                        break;
                    case R.id.intensidad:
                        intensidad= Double.parseDouble(t);
                        break;
                }
            }catch (NumberFormatException e){
                error = getString(R.string.err_number_format);
                b = i;
                full = false;
                break;
            }catch (EmptyStackException e){
                error = getString(R.string.err_fill_data_first);
                b = i;
                full = false;
                break;
            }catch (IndexOutOfBoundsException e){
                error = getString(R.string.err_area_);
                b = i;
                full = false;
                break;
            }
        }
        if(!full){
            EditText t = ((EditText) findViewById(b));
            t.setError(error);
            t.requestFocus();
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
        }
        return full;
    }

    public void determinar_intensidad(View view) {

        if (!checkData(new int[]{R.id.longitud, R.id.pendiente, R.id.hp}))
            return;

        tc = Calculator.tc(pendiente, longitud);

        Intent i = new Intent(this, DeterminarIntensidad.class);
        i.putExtra(Calculator.TC, tc);
        i.putExtra(Calculator.HP, hp);
        startActivityForResult(i, FOR_RESULT_INTENSIDAD);

    }
}
