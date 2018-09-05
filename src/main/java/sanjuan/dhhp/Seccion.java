package sanjuan.dhhp;

import android.content.Context;
import android.media.Image;
import android.view.View;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.Serializable;

import static java.lang.Math.abs;
import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 * Created by San Juan on 30/04/2018 to DHHP.
 */

class Seccion implements Serializable{


    public double getGasto(double pendiente) {
        return (1/rugosidad)*getArea()*Math.pow(getRadio(), 0.666666666666667)*Math.sqrt(pendiente);
    }

    public static double getGasto(Seccion seccion, double pendiente) {
        return (1/seccion.rugosidad)*seccion.getArea()*Math.pow(seccion.getRadio(), 0.666666666666667)*Math.sqrt(pendiente);
    }

    public static double getGasto(double pendiente, double radio, double area, double rugosidad) {
        return (1/rugosidad)*area*Math.pow(radio, 0.666666666666667)*Math.sqrt(pendiente);
    }

    public static double getPerimetroMojado(Seccion seccion){
        return  getPerimetroMojado(seccion.ancho, seccion.prof, seccion.profAnt);
    }

    public static double getPerimetroMojado(double ancho, double prof, double profAnt) {
        double Pi = 0;
        switch (getForma(prof, profAnt)){
            case Triangulo:
                Pi = Math.sqrt(Math.pow(ancho, 2) + Math.pow(max(prof, profAnt), 2));
                break;
            case Rectangulo:
                Pi = ancho;
                break;
            case Trapecio:
                Pi = getPerimetroMojado(ancho, abs(prof-profAnt), 0);
                break;
        }

        return Pi;
    }

    public double getPerimetroMojado() {
        double Pi = 0;
        switch (getForma(prof, profAnt)){
            case Triangulo:
                Pi = Math.sqrt(Math.pow(ancho, 2) + Math.pow(max(prof, profAnt), 2));
                break;
            case Rectangulo:
                Pi = ancho;
                break;
            case Trapecio:
                Pi = getPerimetroMojado(ancho, abs(prof-profAnt), 0);
                break;
        }

        return Pi;
    }

    public static double getRadio(double area, double perimetro) {
        return area / perimetro;
    }

    public static double getRadio(Seccion seccion) {
        return seccion.getArea() / seccion.getPerimetroMojado();
    }

    public double getRadio() {
        return getArea()/ getPerimetroMojado();
    }

    public double getProfMax() {
        return prof>profAnt?prof:profAnt;
    }


    public enum Formas{Triangulo, Rectangulo, Trapecio};

    public double rugosidad;
    public double ancho; //ancho de la session = altura de figura
    public double prof;  //progundidad mas larga = prof o lado mas largo de la figura
    public double profAnt = 0; //progundidad mas corto = prof o lado mas corto de la figura

    public Seccion(double rugosidad, double ancho, double prof, double profAnt){
        init(rugosidad, ancho, prof, profAnt);
    }


    private void init(double rugosidad, double ancho, double prof, double proAnt){
        this.rugosidad = rugosidad;
        this.ancho = ancho;
        this.prof = prof;
        this.profAnt = proAnt;
    }

    public static Formas getForma(double prof, double proAnt){
        Formas f = Formas.Trapecio;
        if (prof == 0 || proAnt == 0)
            f = Formas.Triangulo;
        else if (prof == proAnt)
            f = Formas.Rectangulo;
        return f;
    }

    public Formas getForma(){
        Formas f = Formas.Trapecio;
        if (prof == 0 || profAnt == 0)
            f = Formas.Triangulo;
        else if (prof == profAnt)
            f = Formas.Rectangulo;
        return f;
    }

    public double getArea(){
        return getArea(ancho, prof, profAnt);
    }

    static public double getArea(Seccion seccion){
        return getArea(seccion.ancho, seccion.prof, seccion.profAnt);
    }

    static public double getArea(double ancho, double prof, double profAnt){
        double result;

        if (Seccion.getForma(prof, profAnt) == Formas.Rectangulo){
            result = ancho * prof;
        }else{
            result = ((prof+profAnt)/2)*ancho;
        }

        return result;
    }


    @Override
    public String toString() {
        String t = "";
        t += "< Rugosidad: "+rugosidad+" Ancho: "+ancho+" Profundidad izquierda: "+profAnt + " Profundidad derecha: "+prof+">";
        return t;
    }
}
