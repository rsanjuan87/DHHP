package sanjuan.dhhp;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import java.util.EmptyStackException;

import static sanjuan.dhhp.Utils.format;

public class LucesParciales extends AppCompatActivity {

    private double[] luces;
    int filled = 0;
    private Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_luces_parciales);
        extras = getIntent().getExtras();
    }

    public void adicionar_onclick(View view) {
        if (filled<luces.length)
            addLuz("", filled);
    }

    private void addLuz(String luz, final int i) {
        final EditText luzE = new EditText(this);
        luzE.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        luzE.setText(luz);


        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle(getString(R.string.insert_long_part, i+1));
        LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        ll.setMargins(10, 10, 10, 10);
        LinearLayout l = new LinearLayout(this);
        l.setLayoutParams(ll);
        l.setOrientation(LinearLayout.VERTICAL);
        luzE.setLayoutParams(ll);
        ScrollView sc = new ScrollView(this);
        sc.setLayoutParams(ll);
        sc.addView(luzE);
        b.setView(l);
        final DialogInterface.OnClickListener OkClick = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String error = "";
                boolean full = true;
                double luzD = 0;
                try {
                    for (EditText et : new EditText[]{luzE}) {
                        if (et.getText().toString().isEmpty())
                            throw new EmptyStackException();
                    }
                    luzD = Double.parseDouble(luzE.getText().toString());
                } catch (NumberFormatException e) {
                    error = getString(R.string.err_number_format);
                    full = false;
                } catch (EmptyStackException e) {
                    error = getString(R.string.err_fill_data_first);
                    full = false;
                }
                if (!full) {
                    Toast.makeText(LucesParciales.this, error, Toast.LENGTH_SHORT).show();
                    addLuz(luzE.getText().toString(), i);
                } else {
                    setLuz(luzD);
                }
            }
        };
        b.setPositiveButton(R.string.insertar, OkClick);
        b.setNeutralButton(getString(R.string.cantidad), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                introCantLuzPart("");
            }
        });
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

    public void setLuz(double luz) {
        luces[filled++]= luz;
        if (filled<luces.length)
            addLuz("", filled);
        else{
            double r = 0;
            for (double d :luces) {
                r+=d;
            }
            double longT = extras.getDouble(Calculator.LONG_ESTRIBO);
            if (r!=longT) {
                Toast.makeText(this, R.string.err_dimes_not_equals, Toast.LENGTH_SHORT).show();
                introCantLuzPart("");
            }else{
                Intent i = new Intent(this, RemansoActivity.class);
                extras.putInt(Calculator.CANT_LUCES, luces.length);
                extras.putDoubleArray(Calculator.LUZ, luces);
                i.putExtras(extras);
                startActivity(i);
                finish();
            }

        }

    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        introCantLuzPart("");
        super.onPostCreate(savedInstanceState);
    }

    private void introCantLuzPart(String luz) {
        final EditText luzE = new EditText(this);
        luzE.setInputType(InputType.TYPE_CLASS_NUMBER);
        luzE.setText(luz);


        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle(getString(R.string.insert_cant_luces_part, format(extras.getDouble(Calculator.LONG_ESTRIBO))));
        LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        ll.setMargins(10, 10, 10, 10);
        LinearLayout l = new LinearLayout(this);
        l.setLayoutParams(ll);
        l.setOrientation(LinearLayout.VERTICAL);
        luzE.setLayoutParams(ll);
        ScrollView sc = new ScrollView(this);
        sc.setLayoutParams(ll);
        sc.addView(luzE);
        b.setView(l);
        final DialogInterface.OnClickListener OkClick = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String error = "";
                boolean full = true;
                int luzD = 0;
                try {
                    for (EditText et : new EditText[]{luzE}) {
                        if (et.getText().toString().isEmpty())
                            throw new EmptyStackException();
                    }
                    luzD = Integer.parseInt(luzE.getText().toString());
                } catch (NumberFormatException e) {
                    error = getString(R.string.err_number_format);
                    full = false;
                } catch (EmptyStackException e) {
                    error = getString(R.string.err_fill_data_first);
                    full = false;
                }
                if (!full) {
                    Toast.makeText(LucesParciales.this, error, Toast.LENGTH_SHORT).show();
                    introCantLuzPart(luzE.getText().toString());
                } else {
                    setCantLuz(luzD);
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

    private void setCantLuz(int cantLucesParciales) {
        luces = new double[cantLucesParciales];
        filled=0;
        addLuz("", 0);
    }

    public void finish(View view) {

    }
}
