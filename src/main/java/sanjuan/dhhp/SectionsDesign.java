package sanjuan.dhhp;

import java.io.Serializable;
import java.util.LinkedList;

/**
 * Created by San Juan on 02/05/2018 to DHHP.
 */

class SectionsDesign extends LinkedList<Seccion> implements Serializable{

    public double getLastProf() {
        double result = 0;
        try{
            result = getLast().prof;
        }catch (Exception e){
            e.printStackTrace();
        }

        return result;
    }


    public double getAreaAcumulada(long id) {
        double area = 0;
        for (int i = 0; i < id; i++) {
            area+= get(i).getArea();
        }
        return area;
    }

    public double getGastoAcumulado(long id, double pendiente) {
        double sum = 0;
        for (int i = 0; i < id; i++) {
            sum+= get(i).getGasto(pendiente);
        }
        return sum;
    }

    public double getGastoAcumuladoTotal(double pendiente) {
        return getGastoAcumulado(size(), pendiente);
    }

    public double getAreaAcumuladaTotal() {
        return getAreaAcumulada(size());
    }

    public double getProfundidadMax() {
        double result = 0;
        for (Seccion s: this) {
            if(s.prof > result)
                result = s.prof;
            if(s.profAnt > result)
                result = s.profAnt;
        }
        return result;
    }

    public double getAreaAcumulada(int estriboIpos, int estriboFpos) {
        return getAreaAcumulada(estriboFpos+1) - getAreaAcumulada(estriboIpos);
    }

    public double getAnchoTotal() {
        double result = 0;
        for (Seccion s : this) {
            result += s.ancho;
        }
        return result;
    }

    public double getInicio(int pos) {
        double result = 0;
        for (int i = 0; i < pos; i++) {
            result += this.get(i).ancho;
        }
        return result;
    }


    public double getLongitudAcumulada(int estriboIpos, int estriboFpos) {
        double r = 0;
        for (int i = estriboIpos; i <= estriboFpos; i++) {
            r+=get(i).ancho;
        }
        return r;
    }
}
