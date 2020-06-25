package com.gibsoncodes.newsapp.common

import android.graphics.*
import com.squareup.picasso.Transformation
/**
 * original source code written by wasabeef
 */
enum class CornerType{
    ALL,
    RIGHT, LEFt,
    TOP, BOTTOM
}
class RoundedCornerTransformation(private val radius:Int, private val margin:Int,private val cornerType: CornerType): Transformation {
   // constructor()
private val diameter:Int
    init{
        diameter= radius*2
    }
    override fun transform(source: Bitmap?): Bitmap {
        val width= source?.width
        val height= source?.height
        val bitmap:Bitmap= Bitmap.createBitmap(width!!,
            height!!,Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val paint= Paint()
        paint.isAntiAlias=true
        paint.shader= BitmapShader(source, Shader.TileMode.CLAMP,
            Shader.TileMode.CLAMP)
        drawRoundRect(canvas,paint,width.toFloat(),height.toFloat())
        source.recycle()
        return bitmap
    }
    private fun drawRoundRect(canvas: Canvas,paint:Paint,
                              width:Float,height:Float){
        val right= width-margin
        val bottom= height-margin
        when(cornerType){
            CornerType.ALL->{
                canvas.drawRoundRect(RectF(margin.toFloat(),
                    margin.toFloat(),right,bottom),radius.toFloat(),radius.toFloat(),paint)}
            CornerType.BOTTOM->drawBottomRoundRect(canvas, paint, right, bottom)
            CornerType.TOP-> drawTopRoundRect(canvas, paint, right, bottom)
            CornerType.LEFt->drawLeftRoundRect(canvas, paint, right, bottom)
            CornerType.RIGHT-> drawRightRoundRect(canvas, paint, right, bottom)
        }
    }
    private fun drawRightRoundRect(canvas: Canvas,paint: Paint,right:Float,bottom:Float){
        canvas.drawRoundRect(RectF(right - diameter, margin.toFloat(), right, bottom), radius.toFloat(), radius.toFloat(),
        paint);
        canvas.drawRect(RectF(margin.toFloat(), margin.toFloat(), right - radius, bottom), paint);
    }
    private fun drawLeftRoundRect(canvas: Canvas,paint: Paint,right:Float,bottom:Float){
        canvas.drawRoundRect(RectF(margin.toFloat(), margin.toFloat(), (margin + diameter).toFloat(), bottom),
            radius.toFloat(), radius.toFloat(),
        paint)
        canvas.drawRect( RectF((margin + radius).toFloat(), margin.toFloat(), right, bottom), paint)
    }
    private fun drawBottomRoundRect(canvas: Canvas,paint: Paint,right:Float,bottom:Float){
        canvas.drawRoundRect(RectF(margin.toFloat(), bottom - diameter, right, bottom), radius.toFloat(), radius.toFloat(),
        paint)
        canvas.drawRect(RectF(margin.toFloat(), margin.toFloat(), right, bottom - radius), paint);
    }
private fun drawTopRoundRect(canvas: Canvas,paint: Paint,right:Float,bottom:Float){
    canvas.drawRoundRect(RectF(margin.toFloat(),margin.toFloat(),right.toFloat(),
        (margin+diameter).toFloat()),radius.toFloat(),radius.toFloat(),paint)
    canvas.drawRect(RectF(margin.toFloat(),(margin+radius).toFloat(),right,bottom),paint)
}
    override fun key(): String {
        return "RoundedTransformation(radius= ${radius},margin = ${margin} ,diameter= ${diameter},cornerType=${cornerType.name}"
    }


}