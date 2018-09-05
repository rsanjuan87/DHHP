package sanjuan.dhhp;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Serializable;

import static java.lang.Math.pow;

/**
 * Created by San Juan on 30/04/2018 to DHHP.
 */

class Calculator implements Serializable {
    public static final Double KD = 16.67; // COEFICIENTE DEFINIDO
    private static final double CONST_SEGURIDAD = 0.25;
    public static final String LUZ = "LUZ";
    public static final String PENDIENTE = "PENDIENTE";
//    public static final String COEFICIENTE = "COEFICIENTE";
    public static final String LONGITUD = "LONGITUD";
    public static final String AREA = "AREA";
    public static final String HP = "HP";
    public static final String FACTOR_PROB = "FACTOR_PROB";
    public static final String NIVEL_CRECIDA = "NIVEL_CRECIDA";
    public static final String TC = "TC";
    public static final String INTENSIDAD = "INTENSIDAD";
    public static final String GASTO_ACUMULADO = "GASTO_ACUMULADO";
    public static final String AREA_ACUMULADA = "AREA_ACUMULADA";
    public static final String PROF_MAX = "PROF_MAX";
    public static final String GASTO_CAUSE_PRINCIPAL = "GASTO_CAUSE_PRINCIPAL";
    public static final String CANT_SECCIONES = "CANT_SECCIONES";
    public static final String SECCIONES = "SECCIONES";
    public static final String ESTRIBO_INICIO = "ESTRIBO_INICIO";
    public static final String ESTRIBO_FINAL = "ESTRIBO_FINAL";
    public static final String AREA_ESTRIBO = "AREA_ESTRIBO";
    public static final String MIU = "MIU";
    public static final String ESPESOR_LOSA = "ESPESOR_LOSA";
    public static final String ESPESOR_VIGA = "ESPESOR_VIGA";
    public static final String ESPESOR_PAVIMENTO = "ESPESOR_PAVIMENTO";
    public static final String LONG_ESTRIBO = "LONG_ESTRIBO";
    public static final String CANT_LUCES = "CANT_LUCES";

    public static final String PROYECTO = "PROYECTO";
    public static final String ZONA = "ZONA";
    public static final String OBSERVACIONES = "OBSERVACIONES";

    static double[][] matrix = new double[][]{
            {1.0, 1.0, 1.0, 1.0, 1.0, 1.0},
            {0.96, 0.98, 0.99, 0.99, 1, 1 },
            {0.94, 0.97, 0.97, 0.99, 0.99, 1},
            {0.93, 0.95, 0.97, 0.98, 0.99, 0.99},
            {0.9, 0.94, 0.96, 0.97, 0.98, 0.99},
            {0.89, 0.93, 0.95, 0.96, 0.95, 0.99},
            {0.87, 0.92, 0.94, 0.96, 0.98, 0.99},
            {0.85, 0.91, 0.93, 0.95, 0.97, 0.99}
    };

    /**
     * @param area Area de la cuenca
     * @param coeficiente Coeficiente de escurrimiento
     *@param intensidad Intensidad de presipitaciones
     * */
    public static Double caudalDisenno(Double area, Double coeficiente, Double intensidad, Double factor_prob) {
        return area*coeficiente*intensidad*KD*factor_prob;
    }

    public static double tc(double pendiente, double longitud) {
        return 0.00805*pow(longitud/Math.sqrt(pendiente*100), 0.64)*60;
    }

    public static double coeficienteEscurrimiento(double pendiente){
        double result = 0;
        if (pendiente <= 0.01){
            result = 0.65;
        }else if (0.001 < pendiente && pendiente <= 0.02){
            result = 0.725;
        }else if (0.002 < pendiente && pendiente <= 0.03){
            result = 0.775;
        }else {
            result = 0.85;
        }
        return result;
    }

    public static double getVelocidadMedia(double gastoAcumulado, double areaAcumulada) {
        return gastoAcumulado / areaAcumulada;
    }

    public static double getMiu(double velocidadMedia, double luz) {
        int y = 0;
        int x = 0;
        if (velocidadMedia<1){
            y = 0;
        }else if (velocidadMedia==1){
            y = 1;
        }else if (velocidadMedia > 1 && velocidadMedia <= 1.5){
            y = 2;
        }else if (velocidadMedia > 1.5 && velocidadMedia <= 2){
            y = 3;
        }else if (velocidadMedia > 2 && velocidadMedia <= 2.5){
            y = 4;
        }else if (velocidadMedia > 2.5 && velocidadMedia <= 3){
            y = 5;
        }else if (velocidadMedia > 3 && velocidadMedia <= 3.5){
            y = 6;
        }else { //if (velocidadMedia > 3.5)
            y = 7;
        }

        if (luz <10){
            x = 0;
        }else if (luz >10 && luz <= 15){
            x = 1;
        }else if (luz >15 && luz <= 20){
            x = 2;
        }else if (luz >20 && luz <= 30){
            x = 3;
        }else if (luz >30 && luz <= 50){
            x = 4;
        }else{ //if (luz > 50)
            x = 5;
        }

        return matrix[y][x];
    }

    static public void save(String path, Bundle extras, Context context) throws IOException {
            FileOutputStream fos = null;
            fos = context.openFileOutput(path, Context.MODE_PRIVATE);
            Parcel p = Parcel.obtain(); //creating empty parcel object
            extras.writeToParcel(p, 0); //saving bundle as parcel
            fos.write(p.marshall()); //writing parcel to file
            fos.flush();
            fos.close();
    }

    static public Bundle load(String path, Context context) throws IOException {
        FileInputStream fis = context.openFileInput(path);
        byte[] array = new byte[(int) fis.getChannel().size()];
        fis.read(array, 0, array.length);
        fis.close();
        Parcel parcel = Parcel.obtain();
        parcel.unmarshall(array, 0, array.length);
        parcel.setDataPosition(0);
        Bundle out = parcel.readBundle();
        out.putAll(out);
        parcel.recycle();

        return out;
    }


    public static double getVelocidadMedia2(double gastoAcumulado, double area_estribo, double miu) {
        return gastoAcumulado/(area_estribo * miu);
    }

    /**
     * @return en porciento
     */
    public static double getTcp(double gastoCaucePrincipal, double gastoAcumulado) {
        return (1-(gastoCaucePrincipal/gastoAcumulado))*100;
    }

    /**
     *
     * @param tcp en porciento
     * @return
     */
    public static double getCoeficienteRemanso(double tcp) {
        double result = 0;
        if (tcp < 21){
            result = 0.06;
        }else if (tcp >= 21 && tcp <41){
            result = 0.085;
        }else if (tcp >= 41 && tcp <61){
            result = 0.115;
//        }else if (tcp >= 61 && tcp <81){
//            result = 0.15;
        }else {
            result =0.15; // verificar para + 81
        }
        return result;
    }

    /**
     * @param coeficienteRemanso
     * @param velocidadMedia
     * @param velocidadMedia2
     * @return
     */
    public static double alturaRemaso(double coeficienteRemanso, double velocidadMedia, double velocidadMedia2) {
        return coeficienteRemanso*(pow(velocidadMedia2,2)-pow(velocidadMedia,2));
    }

    /**
     *
     * @param alturaRemanso
     * @return
     */
    public static double getAltulaLibreVertical(double alturaRemanso) {
        double result =0;
        if (alturaRemanso < 0.5){
            result = 0.5;
        }else if(alturaRemanso >= 0.5 && alturaRemanso < 1 ){
            result = 0.6;
        }else {
            result = 0.7;
        }
        return result;
    }

    public static double alturaTablero(double espesorLosa, double espesorPavimento, double espesorViga) {
        return espesorLosa+espesorPavimento+espesorViga;
    }

    public static double nivelRasanteMin(double nivel_crecida, double alturaRemanso, double altuLibreVertical, double alturaTablero) {
        return nivel_crecida+alturaRemanso+altuLibreVertical+alturaTablero+CONST_SEGURIDAD;
    }

    public static double nivelRemansoMax(double nivel_crecida, double alturaRemanso) {
        return nivel_crecida+alturaRemanso;
    }

    public static double semejanza(double profI, double profF, double ancho){
        double result = 0;
            result = profF*ancho;
            result = result/profI;
            return result;
    }

    public static void writeToFile(String proyecto, String data, Context context) throws IOException {
            File f = new File("/sdcard/"+context.getString(R.string.app_name)+"/" );
            f.mkdirs();
            File myFile = new File("/sdcard/"+context.getString(R.string.app_name)+"/" + proyecto + ".txt");
            myFile.createNewFile();
            FileOutputStream fOut = new FileOutputStream(myFile);
            OutputStreamWriter myOutWriter =
                    new OutputStreamWriter(fOut);
            myOutWriter.append(data);
            myOutWriter.close();
            fOut.close();

    }
}
