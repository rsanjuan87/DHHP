package sanjuan.dhhp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

/**
 * TODO: document your custom view class.
 */
public class SeccionTranversal extends View {
    private static final float TEXT_SIZE = 24;
    private String mExampleString = "Ejemplo, no se aplica al diseño actual";

    private TextPaint mTextPaint;
    private float mTextWidth;
    private float mTextHeight;

    private SectionsDesign secciones = new SectionsDesign();
    private boolean customSecciones = false;

    Paint stroke;
    Paint wallpaint;
    Path wallpath = new Path();


    public SeccionTranversal(Context context) {
        super(context);
        init(null, 0);
    }

    public SeccionTranversal(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public SeccionTranversal(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    public void setSecciones(SectionsDesign secciones) {
        customSecciones = true;
        this.secciones = secciones;
        invalidate();
    }

    private void init(AttributeSet attrs, int defStyle) {
        secciones.add(new Seccion(0.03, 8, 1.5,0));
        secciones.add(new Seccion(0.03, 10, 3,1.5));
        secciones.add(new Seccion(0.03, 15, 2,3));
        secciones.add(new Seccion(0.03, 12, 0,2));

        stroke = new Paint();
        stroke.setColor(Color.BLUE);
        stroke.setStyle(Paint.Style.STROKE);
        stroke.setStrokeWidth(2);
        wallpaint = new Paint();
        wallpaint.setColor(Color.GRAY);
        wallpaint.setStyle(Paint.Style.FILL);

        // Set up a default TextPaint object
        mTextPaint = new TextPaint();
        mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextAlign(Paint.Align.LEFT);
        mTextPaint.setTextSize(TEXT_SIZE);

        // Update TextPaint and text measurements from attributes
        invalidateTextPaintAndMeasurements();
    }

    private void invalidateTextPaintAndMeasurements() {
        mTextPaint.setColor(Color.RED);
        mTextWidth = mTextPaint.measureText(mExampleString);

        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        mTextHeight = fontMetrics.bottom;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // TODO: consider storing these as member variables to reduce
        // allocations per draw cycle.
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        int contentWidth = getWidth() - paddingLeft - paddingRight;
        int contentHeight = getHeight() - paddingTop - paddingBottom;

        double anchoD = secciones.getAnchoTotal();
        double altoD = secciones.getProfundidadMax();

        double ratiox = Math.max(contentWidth,anchoD)/ Math.min(anchoD,contentWidth);
        double ratioy = Math.max(contentHeight,altoD)/ Math.min(altoD,contentHeight);
        float ratio = (float) Math.min(ratiox, ratioy);

        float despY = 5;
        float despX = 0;

        double inicio = 0;


        for (int i = 0; i < secciones.size(); i++) {
            Seccion s = secciones.get(i);
            inicio = secciones.getInicio(i);

            wallpath.reset(); // only needed when reusing this path for a new build
            wallpath.moveTo(((float) (despX + inicio) * ratio),despY*ratio);
            wallpath.lineTo(((float) (despX + inicio+s.ancho) * ratio),(despY)*ratio);
            wallpath.lineTo(((float) (despX + inicio+s.ancho) * ratio), (float) ((despY+s.prof)*ratio));
            wallpath.lineTo(((float) (despX + inicio) * ratio), (float) ((despY+s.profAnt)*ratio));
            wallpath.close();


            canvas.drawPath(wallpath, wallpaint);
            canvas.drawPath(wallpath, stroke);

            RectF r = new RectF(((float) (despX + inicio) * ratio), despY * ratio, ((float) (despX + inicio + s.ancho) * ratio), ((float) (despY + s.profAnt) * ratio));
//            canvas.drawRect(r, wallpaint);
//            canvas.drawRect(r, stroke);
        }


        // Draw the text.
        if (!customSecciones)
            canvas.drawText(mExampleString,
                paddingLeft + (contentWidth - mTextWidth) / 2,
                paddingTop + (contentHeight + mTextHeight) / 2,
                mTextPaint);

        // Draw the example drawable on top of the text.
//        if (mExampleDrawable != null) {
//            mExampleDrawable.setBounds(paddingLeft, paddingTop,
//                    paddingLeft + contentWidth, paddingTop + contentHeight);
//            mExampleDrawable.draw(canvas);
//        }
    }







}
