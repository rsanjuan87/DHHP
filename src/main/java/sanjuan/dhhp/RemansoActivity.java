package sanjuan.dhhp;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.EmptyStackException;

import static sanjuan.dhhp.Utils.format;

public class RemansoActivity extends AppCompatActivity {

    private Bundle extras;
    private double pendiente;
    private double gastoCaucePrincipal;
    private double areaAcumulada;
    private double gastoAcumulado;
    private double area_estribo;
    private double velocidadMedia;
//    private double luz;
    private double miu;
    private double espesorLosa;
    private double espesorViga;
    private double espesorPavimento;
    private double velocidadMedia2;
    private double tcp;
    private double coeficienteRemanso;
    private double alturaRemanso;
    private double altuLibreVertical;
    private double alturaTablero;
    private double nivel_crecida = 0;
    private double nivelRasanteMin;
    private double nivelMaxRemanso;
    private TextView showData;
    private SeccionTranversal sectionsView;
    private double[] luces;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remanso);

        showData = (TextView) findViewById(R.id.zoomIn);
        sectionsView = (SeccionTranversal) findViewById(R.id.sections);

        getData(getIntent().getExtras());
    }

    private void getData(Bundle extras) {
        this.extras = extras;
        pendiente = extras.getDouble(Calculator.PENDIENTE, 0l);
        gastoAcumulado = extras.getDouble(Calculator.GASTO_ACUMULADO, 0l);
        areaAcumulada = extras.getDouble(Calculator.AREA_ACUMULADA, 0l);
        gastoCaucePrincipal = extras.getDouble(Calculator.GASTO_CAUSE_PRINCIPAL, 0l);
        area_estribo = extras.getDouble(Calculator.AREA_ESTRIBO, 0l);
        velocidadMedia = Calculator.getVelocidadMedia(gastoAcumulado, areaAcumulada);
        nivel_crecida = extras.getDouble(Calculator.NIVEL_CRECIDA, nivel_crecida);
        luces = extras.getDoubleArray(Calculator.LUZ);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        introduceLuz("", "", "");
    }


    private void introduceLuz(String losa, String viga, String pavimento) {
        LinearLayout cV = (LinearLayout) getLayoutInflater().inflate(R.layout.intro_luz, null);
//        final EditText luzE = cV.findViewById(R.id.zoomIn);
//        luzE.setText(luz);
        final EditText losaE = cV.findViewById(R.id.esperor_losa);
        losaE.setText(losa);
        final EditText vigaE = cV.findViewById(R.id.esperor_viga);
        vigaE.setText(viga);
        final EditText pavimentoE = cV.findViewById(R.id.esperor_pavimento);
        pavimentoE.setText(pavimento);

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
                double losaD = 0;
                double vigaD = 0;
                double pavimentoD = 0;
                try {
                    for (EditText et : new EditText[]{
//                            luzE,
                            losaE, vigaE, pavimentoE}) {
                        if (et.getText().toString().isEmpty())
                            throw new EmptyStackException();
                    }
//                    luzD = Double.parseDouble(luzE.getText().toString());
                    losaD = Double.parseDouble(losaE.getText().toString());
                    vigaD = Double.parseDouble(vigaE.getText().toString());
                    pavimentoD = Double.parseDouble(pavimentoE.getText().toString());
                } catch (NumberFormatException e) {
                    error = getString(R.string.err_number_format);
                    full = false;
                } catch (EmptyStackException e) {
                    error = getString(R.string.err_fill_data_first);
                    full = false;
                }
                if (!full) {
                    Toast.makeText(RemansoActivity.this, error, Toast.LENGTH_SHORT).show();
                    introduceLuz(losaE.getText().toString(), vigaE.getText().toString(), pavimentoE.getText().toString());
                } else {
                    setLuz(losaD, vigaD, pavimentoD);
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

    public void setLuz(double losaD, double vigaD, double pavimentoD) {
//        this.luces = luces;
        double miuTotal = 0;
        for (double s : luces) {
            miuTotal+=Calculator.getMiu(velocidadMedia, s);
        }
        miu = miuTotal/luces.length;

        this.espesorLosa = losaD;
        this.espesorViga = vigaD;
        this.espesorPavimento = pavimentoD;

        alturaTablero = Calculator.alturaTablero(espesorLosa, espesorPavimento, espesorViga);
        velocidadMedia2 = Calculator.getVelocidadMedia2(gastoAcumulado, area_estribo, miu);
        tcp = Calculator.getTcp(gastoCaucePrincipal, gastoAcumulado);
        coeficienteRemanso = Calculator.getCoeficienteRemanso(tcp);
        alturaRemanso = Calculator.alturaRemaso(coeficienteRemanso, velocidadMedia, velocidadMedia2);
        altuLibreVertical = Calculator.getAltulaLibreVertical(alturaRemanso);
        nivelRasanteMin = Calculator.nivelRasanteMin(nivel_crecida, alturaRemanso, altuLibreVertical, alturaTablero);
        nivelMaxRemanso = Calculator.nivelRemansoMax(nivel_crecida, alturaRemanso);

//        extras.putDouble(Calculator.LUZ, luz);
        extras.putDouble(Calculator.MIU, miu);
        extras.putDouble(Calculator.ESPESOR_LOSA, espesorLosa);
        extras.putDouble(Calculator.ESPESOR_VIGA, espesorViga);
        extras.putDouble(Calculator.ESPESOR_PAVIMENTO, espesorPavimento);
        extras.putDouble(Calculator.ESPESOR_PAVIMENTO, espesorPavimento);


        showData.setText(getString(R.string.remanso_data,
                format(nivelRasanteMin),
                format(nivelMaxRemanso),
                format(altuLibreVertical),
                format(alturaRemanso),
                format(alturaTablero)));

        try{
            SectionsDesign sections = new SectionsDesign();
            Bundle sBun = extras.getBundle(Calculator.SECCIONES);
            for (int i = 0; i < extras.getInt(Calculator.CANT_SECCIONES); i++) {
                sections.add((Seccion) sBun.getSerializable(String.valueOf(i)));
            }
            sectionsView.setSecciones(sections);
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    public void finish(View view) {
        Intent i = new Intent(this, SaveActy.class);
        i.putExtras(extras);
        startActivity(i);
    }
}
