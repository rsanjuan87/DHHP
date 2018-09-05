package sanjuan.dhhp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DeterminarIntensidad extends AppCompatActivity {

    private double tc;
    private ImageView image;
    private float zoom = 1;
    private double hp = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_determinar_intensidad);


        Intent i = getIntent();
        tc = i.getDoubleExtra(Calculator.TC, 0L);
        hp = i.getDoubleExtra(Calculator.HP, 0L);
        ((TextView) findViewById(R.id.tc)).setText(getString(R.string.tiempo_de_concentraci_n_1s,
                Utils.format(tc),
                Utils.format(hp)));

        image = (ImageView) findViewById(R.id.image);

    }

    public void exit(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }

    public void finish(View view) {
        TextView v = (TextView) findViewById(R.id.intensidad);
        try{
        String t = v.getText().toString();
        if (t.isEmpty()){
            v.setError("Inserte el dato para continuar");
            v.requestFocus();
        }else{
            Intent i = new Intent();
            i.putExtra(Calculator.INTENSIDAD, Double.parseDouble(t));
            setResult(RESULT_OK, i);
            finish();
        }
        }catch (NumberFormatException e){
            v.setError("Formato de número no válido");
            v.requestFocus();
        }
    }

    public void zoom(View view) {
        zoom= (float) (view.getId()==R.id.zoomIn? zoom+0.25 : zoom-0.25);
        image.setScaleX(zoom);
        image.setScaleY(zoom);
    }
}
