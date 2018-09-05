package sanjuan.dhhp;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EmptyStackException;
import java.util.LinkedList;
import java.util.List;

import static sanjuan.dhhp.Utils.format;

public class MainActivity extends AppCompatActivity {

    private TableLayout tableLayout;
    String headers[] = {"Sección", "Ni", "Ai (m\u00B2)", "Pi (m)", "Ri (m)", "∑A (m)", "Qi (m\u00B3/s)","∑Q (m\u00B3/s)"};

    private double area;
    private double longitud;
    private double pendiente;
    private double coeficiente;
    private double hp;
    private double factor_prob;
    private double nivel_crecida;
    private double tc; //Tiempo de concentracion
    private double intensidad;
    private SectionsDesign seccions;
    private TextView caudalView;
    private TextView rangoView;
    private double RangMin;
    private double RangMax;
    private TableRow selectedRow = null;
    private Double caudalDisenno;
    private Integer causePrincFinal = null;
    private Integer causePrincInicio = null;
    private Spinner caucePF;
    private Spinner caucePI;
    private int cant_secciones;
    private Bundle extras;
    private Spinner estriboI;
    private Spinner estriboF;
    private int estriboIpos;
    private int estriboFpos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tableLayout = (TableLayout) findViewById(R.id.table);
        seccions = new SectionsDesign();

        caucePI = ((Spinner)findViewById(R.id.causePI));
        caucePI.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                causePrincInicio = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                causePrincInicio = null;
            }
        });
        caucePF = ((Spinner)findViewById(R.id.causePF));
        caucePF.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                causePrincFinal = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                causePrincFinal = null;
            }
        });


        estriboI = ((Spinner)findViewById(R.id.estriboI));
        estriboI.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                estriboIpos = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                causePrincInicio = null;
            }
        });
        estriboF = ((Spinner)findViewById(R.id.estriboF));
        estriboF.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                estriboFpos = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                causePrincInicio = null;
            }
        });

        setHeaders(Arrays.asList(headers));

        getData(getIntent());



//        test();
    }

    private void test() {
        addSecction(0.03, 8, 0,1.5,true,0);
        addSecction(0.03, 12, 1.5,3,true,1);
        addSecction(0.035, 15, 3,2,true,2);
        addSecction(0.03, 10, 2,0,true,3);
    }

    private void getData(Intent intent) {
        extras = intent.getExtras();
        pendiente = intent.getDoubleExtra(Calculator.PENDIENTE, 0l);
//        cant_secciones = intent.getIntExtra(Calculator.CANT_SECCIONES, 0);
        longitud = intent.getDoubleExtra(Calculator.LONGITUD, 0l);
        area = intent.getDoubleExtra(Calculator.AREA, 0l);
        hp = intent.getDoubleExtra(Calculator.HP, 0l);
        factor_prob = intent.getDoubleExtra(Calculator.FACTOR_PROB, 0l );
//        nivel_crecida = intent.getDoubleExtra(Calculator.NIVEL_CRECIDA, 0l);
        tc = intent.getDoubleExtra(Calculator.TC, 0L);
        intensidad = intent.getDoubleExtra(Calculator.INTENSIDAD, 0L);
    }


    private TableRow createRow(TableLayout tableLayout) {
        TableRow row = new TableRow(this);
//        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
//        row.setLayoutParams(lp);
        row.setShowDividers(tableLayout.getShowDividers());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            row.setDividerDrawable(getResources().getDrawable(R.drawable.line));
        }
        row.setDividerPadding(tableLayout.getDividerPadding());
        return row;
    }

    private TextView createCell(String text) {
        TextView view = new TextView(this);
        view.setText(" "+text+" ");
        view.setGravity(Gravity.CENTER);
//        view.setBackgroundColor(Color.WHITE);
        int d = (int) getResources().getDimension(R.dimen.appbar_padding_top);
        view.setPadding(d,d,d,d);
        return view;
    }

    private TextView createHeaderView(String header) {
        TextView view = createCell(header);
        view.setBackgroundColor(Color.LTGRAY);
        return view;
    }


    public void adicionar_onclick(final View view) {
        if (!canAddSection()) {
            corregir("");
        } else {
            addicionar("","","","");
        }
    }

    private void addicionar(String profP, String profAP, String rugP, String anchoP) {
        LinearLayout cV = (LinearLayout) getLayoutInflater().inflate(R.layout.add_seccion, null);
        final EditText rugosidad = (EditText) cV.findViewById(R.id.rugosidad);
        rugosidad.setText(rugP);
        final EditText prof = (EditText) cV.findViewById(R.id.prof);
        prof.setText(profP);
        final EditText profAnt = cV.findViewById(R.id.profAnt);
        profAnt.setText(profAP);
        final EditText ancho = (EditText) cV.findViewById(R.id.ancho);
        ancho.setText(anchoP);

        if (seccions.size()==0){
            profAnt.setEnabled(false);
            profAnt.setText("0");
        }
        if (seccions.size()==cant_secciones-1){
            prof.setEnabled(false);
            prof.setText("0");
        }

        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle(getString(R.string.seccion_num, seccions.size()+1 , cant_secciones));
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
        DialogInterface.OnClickListener OkClick = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String error = "";
                boolean full = true;
                double rugD = 0;
                double profD = 0;
                double profAD = 0;
                double anchoD = 0;
                try{
                    for (EditText et : new EditText[]{prof, profAnt, rugosidad, ancho}) {
                        if(et.getText().toString().isEmpty())
                            throw new EmptyStackException();
                    }
                    profD = Double.parseDouble(prof.getText().toString());
                    profAD = Double.parseDouble(profAnt.getText().toString());
                    rugD = Double.parseDouble(rugosidad.getText().toString());
                    anchoD = Double.parseDouble(ancho.getText().toString());
                }catch (NumberFormatException e){
                    error = getString(R.string.err_number_format);
                    full = false;
                }catch (EmptyStackException e){
                    error = getString(R.string.err_fill_data_first);
                    full = false;
                }
                if (!full){
                    Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
                    addicionar(prof.getText().toString(),profAnt.getText().toString(),
                            rugosidad.getText().toString(),ancho.getText().toString());
                }else{
                    addSecction(rugD, anchoD, profD, profAD, true, 0);
                }
            }
        };
        b.setPositiveButton(R.string.adicionar, OkClick);
        b.setNeutralButton(getString(R.string.prev), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        l.addView(sc);
        b.show();
    }

    private void corregir(String proP
//            , String anchoFin, String anchoPrin
    ) {
        LinearLayout cV = (LinearLayout) getLayoutInflater().inflate(R.layout.correccion, null);
        final EditText prof = (EditText) cV.findViewById(R.id.prof);
        prof.setText(proP);
        prof.setHint(getString(R.string.ej_1_s_m_2_s_m, format(nivel_crecida), String.valueOf(5)));
//        final EditText anchoP = (EditText) cV.findViewById(R.id.anchoP);
//        anchoP.setHint(getString(R.string.ancho_actual_1_s_m, format(seccions.get(0).ancho)));
//        anchoP.setText(anchoPrin);
//        final EditText anchoF = cV.findViewById(R.id.anchoF);
//        anchoF.setHint(getString(R.string.ancho_actual_1_s_m, format(seccions.get(cant_secciones-1).ancho)));
//        anchoF.setText(anchoFin);
        final Spinner sp = (Spinner) cV.findViewById(R.id.operation);

        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle(getString(R.string.correccion_disenno_title));
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
        DialogInterface.OnClickListener OkClick = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String error = "";
                boolean full = true;
                double profD = 0;
                double anchoFD = 0;
                double anchoPD = 0;
                try{
                    for (EditText et : new EditText[]{prof}) {
                        if(et.getText().toString().isEmpty())
                            throw new EmptyStackException();
                    }
                    profD = Double.parseDouble(prof.getText().toString());
                    profD = sp.getSelectedItemPosition()==0? profD : -profD;
                    if ( seccions.get(0).prof + profD < 0 ){
                        Toast.makeText(MainActivity.this, R.string.err_nivel_, Toast.LENGTH_LONG).show();
                        return;
                    }
                    //semejansa de triangulos seccion 1
                    anchoPD = Calculator.semejanza(seccions.get(0).getProfMax(),
                            seccions.get(0).getProfMax() + profD,
                            seccions.get(0).ancho);
                    //semejansa de triangulos seccion final
                    anchoFD = Calculator.semejanza(seccions.get(cant_secciones-1).getProfMax(),
                            seccions.get(cant_secciones-1).getProfMax() + profD,
                            seccions.get(cant_secciones-1).ancho);

                }catch (NumberFormatException e){
                    error = getString(R.string.err_number_format);
                    full = false;
                }catch (EmptyStackException e){
                    error = getString(R.string.err_fill_data_first);
                    full = false;
                }
                if (!full){
                    Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
                    corregir(prof.getText().toString());
                }else{
                    setCorreccion(profD, anchoPD,  anchoFD);
                }
            }
        };
        b.setPositiveButton(R.string.corregir, OkClick);
        b.setNeutralButton(getString(R.string.prev), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        b.setNeutralButton(getString(R.string.sections), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                limpiar(sp);
                adicionar_onclick(sp);
            }
        });
        l.addView(sc);
        b.show();
    }

    private void setCorreccion(double profD, double anchoPD, double anchoFD) {
        tableLayout.removeAllViews();
        setHeaders(Arrays.asList(headers));

        for (int i = 0; i < seccions.size(); i++) {
            Seccion s = seccions.get(i);
            if (i == 0) {
                s.ancho = anchoPD;
            }else if( i == seccions.size()-1){
                s.ancho = anchoFD;
            }
            s.prof+=profD;
            s.profAnt+=profD;
            addSecction(s.rugosidad, s.ancho, s.prof, s.profAnt, false, i);
        }
        nivel_crecida+=profD;
        double x = seccions.getGastoAcumuladoTotal(pendiente);
        findViewById(R.id.rango).setVisibility((x < RangMax && x >RangMin)?View.GONE:View.VISIBLE);
        if (cant_secciones == seccions.size())
            Toast.makeText(this, (x < RangMax && x >RangMin)?
                    R.string.se_cumple_la_condici_n_de_parada:R.string.no_se_cumple_la_condici_n_de_parada, Toast.LENGTH_SHORT).show();
    }


    private boolean canAddSection() {
        return cant_secciones> seccions.size();
    }

    private void addSecction(double rugD, double anchoD, double profD, double profAD, boolean add, int index) {
        profAD = Math.max(0, profAD);
        profD = Math.max(0, profD);
        anchoD = Math.max(0, anchoD);

        if (add){
            Seccion s = new Seccion(rugD, anchoD, profD, profAD);
            seccions.add(s);
        }
        ((Button)findViewById(R.id.add)).setText(getString(canAddSection()?R.string.adicionar:R.string.corregir));
        LinkedList<String> datos = new LinkedList<>();
        int i = add? seccions.size():index+1;
        datos.add(String.valueOf(i));
        datos.add(format(rugD));
        double ar = Seccion.getArea(anchoD, profD, profAD);
        datos.add(format(ar));
        double per = Seccion.getPerimetroMojado(anchoD, profD, profAD);
        datos.add(format(per));
        double rad = Seccion.getRadio(ar, per);
        datos.add(format(rad));
        datos.add(format(seccions.getAreaAcumulada(i)));
        double gast = Seccion.getGasto(pendiente, rad, ar, rugD);
        datos.add(format(gast));
        datos.add(format(seccions.getGastoAcumulado(i, pendiente)));

        final TableRow row = addRow(datos);
        row.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                selectedRow = row;
                row.setBackgroundColor(Color.BLUE);
                PopupMenu popup = new PopupMenu(MainActivity.this, v);
                popup.getMenuInflater().inflate(R.menu.section, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        onOptionsItemSelected(item);
                        return true;
                    }
                });

                popup.show();
                popup.setOnDismissListener(new PopupMenu.OnDismissListener() {
                    @Override
                    public void onDismiss(PopupMenu menu) {
                        row.setBackgroundColor(Color.WHITE);
                    }
                });
                return true;
            }
        });

        double x = seccions.getGastoAcumuladoTotal(pendiente);
        findViewById(R.id.rango).setVisibility((x < RangMax && x >RangMin)?View.GONE:View.VISIBLE);
        List<String> spinnerArray =  new ArrayList<>();

        for (int j = 0; j < seccions.size(); j++) {
            spinnerArray.add(String.valueOf(j+1));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        caucePF.setAdapter(adapter);
        caucePI.setAdapter(adapter);
        estriboI.setAdapter(adapter);
        estriboF.setAdapter(adapter);
        if (add && seccions.size()< cant_secciones)
            adicionar_onclick(row);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //TODO

        return super.onOptionsItemSelected(item);
    }

    private TableRow addRow(Collection<String> data){
        TableRow row = createRow(tableLayout);
        for (String header :data) {
            TextView h = createCell(header);
            row.addView(h);
        }
        tableLayout.addView(row);
        return row;
    }

    private void setHeaders(Collection<String> data){
        TableRow row = createRow(tableLayout);
        for (String header :data) {
            TextView h = createHeaderView(header);
            row.addView(h);
        }
        tableLayout.addView(row);
    }


    public void limpiar(View view) {
        seccions.clear();
        tableLayout.removeAllViews();
        setHeaders(Arrays.asList(headers));
        SpinnerAdapter n = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        caucePF.setAdapter(n);
        caucePI.setAdapter(n);
    }

    public void finish(final View view) {

//        LinearLayout cV = new LinearLayout(this);
//        cV.setOrientation(LinearLayout.VERTICAL);
//
//        TextView text = new TextView(this);
//        text.setText("Inserte los números de las secciones que determines sean el Cause Principal, separado por espacio");
//        cV.addView(text);
//
//        final EditText secciones = new EditText(this);
//        cV.addView(secciones);
//        secciones.setHint("Ej: 4 5 6");
//        InputFilter filter = new InputFilter() {
//            @Override
//            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
//                for (int i = start;i < end;i++) {
//                    if (!Character.isDigit(source.charAt(i)) &&
//                            !Character.toString(source.charAt(i)).equals(" "))
//                    {
//                        return "";
//                    }
//                }
//                return null;
//            }
//        };

//        secciones.setFilters(new InputFilter[] { filter });

//        AlertDialog.Builder b = new AlertDialog.Builder(this);
////        b.setTitle(title);
//        LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        ll.setMargins(10, 10, 10, 10);
//        LinearLayout l = new LinearLayout(this);
//        l.setLayoutParams(ll);
//        l.setOrientation(LinearLayout.VERTICAL);
//        cV.setLayoutParams(ll);
//        ScrollView sc = new ScrollView(this);
//        sc.setLayoutParams(ll);
//        sc.addView(cV);
//        b.setView(l);
//        DialogInterface.OnClickListener OkClick = new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                String error = "";
//                boolean full = true;
//                double gastoCaucePrincipal = 0;
//                try{
//                    String sec = secciones.getText().toString();
//                    while (sec.contains("  ")) sec = sec.replaceAll("  ", " ");
//                    if (sec.isEmpty())
//                        throw new EmptyStackException();
//                    for (String s : sec.split(Pattern.quote(" "))) {
//                        int p = Integer.parseInt(s);
//                        if (p >= seccions.getCount()+1 || p <= 0) {
//                            throw new IndexOutOfBoundsException();
//                        }
//
//                        gastoCaucePrincipal = seccions.list.get(p-1).getGasto(pendiente);
//                    }
//
//                }catch (NumberFormatException e){
//                    error = getString(R.string.err_number_format);
//                    full = false;
//                }catch (EmptyStackException e){
//                    error = getString(R.string.err_fill_data_first);
//                    full = false;
//                }catch (IndexOutOfBoundsException e){
//                    error = getString(R.string.err_section_out);
//                    full = false;
//                }
//                if (!full){
//                    Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
//                    finish(view);
//                }else{


        if(seccions.size() != cant_secciones){
            Toast.makeText(this, R.string.err_sections_faltan, Toast.LENGTH_SHORT).show();
            return;
        }else if(causePrincFinal == null || causePrincInicio == null){
            Toast.makeText(this, R.string.selct_cauce_princi, Toast.LENGTH_SHORT).show();
            return;
        }

        double gastoCaucePrincipal = getGastoCaucePrincipal();
        Intent i = new Intent(MainActivity.this, LucesParciales.class);

        extras.putDouble(Calculator.GASTO_ACUMULADO, seccions.getGastoAcumuladoTotal(pendiente));
        extras.putDouble(Calculator.AREA_ACUMULADA, seccions.getAreaAcumuladaTotal());
        extras.putDouble(Calculator.PROF_MAX, seccions.getProfundidadMax());
        extras.putDouble(Calculator.GASTO_CAUSE_PRINCIPAL, gastoCaucePrincipal);
        extras.putDouble(Calculator.NIVEL_CRECIDA, nivel_crecida);
        extras.putInt(Calculator.ESTRIBO_INICIO, estriboIpos);
        extras.putInt(Calculator.ESTRIBO_FINAL, estriboFpos);
        extras.putDouble(Calculator.AREA_ESTRIBO, seccions.getAreaAcumulada(estriboIpos, estriboFpos));
        extras.putDouble(Calculator.LONG_ESTRIBO, seccions.getLongitudAcumulada(estriboIpos, estriboFpos));

        Bundle secListB = new Bundle();
        for (int i1 = 0; i1 < seccions.size(); i1++) {
            secListB.putSerializable(String.valueOf(i1), seccions.get(i1));
        }
        extras.putBundle(Calculator.SECCIONES, secListB);


        i.putExtras(extras);
        startActivity(i);
//        finish();

//                }
//            }
//        };
//        b.setPositiveButton(R.string.adicionar_onclick, OkClick);
//        l.addView(sc);
//        b.show();


    }

    private double getGastoCaucePrincipal() {
        double result = 0;
        for (int i = causePrincInicio;  i<=causePrincFinal; i++)
            result+= seccions.get(i).getGasto(pendiente);
        return result;
    }

    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        indroCantSecNivel("", "");
    }

    private void indroCantSecNivel(String nivel, final String cant) {
        LinearLayout cV = (LinearLayout) getLayoutInflater().inflate(R.layout.intro_nivel_cant_secc, null);
        final EditText nivelE = cV.findViewById(R.id.nivel_crecida);
        nivelE.setText(nivel);
        final EditText cantE = cV.findViewById(R.id.cant_secciones);
        cantE.setText(cant);

        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle(R.string.intro_data);
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
                double nivelD = 0;
                int cantD = 0;
                try {
                    for (EditText et : new EditText[]{nivelE, cantE}) {
                        if (et.getText().toString().isEmpty())
                            throw new EmptyStackException();
                    }
                    nivelD = Double.parseDouble(nivelE.getText().toString());
                    cantD = Integer.parseInt(cantE.getText().toString());
                } catch (NumberFormatException e) {
                    error = getString(R.string.err_number_format);
                    full = false;
                } catch (EmptyStackException e) {
                    error = getString(R.string.err_fill_data_first);
                    full = false;
                }
                if (!full) {
                    Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
                    indroCantSecNivel(nivelE.getText().toString(), cantE.getText().toString());
                } else {
                    setNivel_Cant(nivelD, cantD);
                }
            }
        };
        b.setPositiveButton(R.string.insertar, OkClick);
        l.addView(sc);
        b.setNeutralButton(getString(R.string.prev), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
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

    private void setNivel_Cant(double nivelD, int cantD) {
        nivel_crecida = nivelD;
        cant_secciones = cantD;
        extras.putDouble(Calculator.NIVEL_CRECIDA, nivel_crecida);
        extras.putInt(Calculator.CANT_SECCIONES, cant_secciones);

        coeficiente = Calculator.coeficienteEscurrimiento(pendiente);
        caudalDisenno = Calculator.caudalDisenno(area, coeficiente, intensidad, factor_prob);
        caudalView = (TextView) findViewById(R.id.caudal);

        RangMin = caudalDisenno*0.95;
        RangMax = caudalDisenno*1.05;

        caudalView.setText(getString(R.string.caudal_de_dise_o_1_0_f, format(caudalDisenno), format(RangMin), format(RangMax)));
        adicionar_onclick(estriboF);
    }

}