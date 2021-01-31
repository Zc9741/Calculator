package com.example.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val currentInputNum=StringBuilder()
   //private val numForMemory=StringBuilder()
    private var isNumStart=true

    private val numList= mutableListOf<Int>()
    private val operateList= mutableListOf<String>()

    @ExperimentalStdlibApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //清空
        acBtn.setOnClickListener {
            isNumStart=true
            currentInputNum.clear()
            process_textview.text=""
            currentInputNum.clear()
            result_textview.text="0"
            operateList.clear()
            numList.clear()
        }
        //加减乘除
        textView18.setOnClickListener { operateClicked(it) }
        textView19.setOnClickListener { operateClicked(it) }
        textView20.setOnClickListener { operateClicked(it) }
        textView21.setOnClickListener { operateClicked(it) }
        //数字
        textView9.setOnClickListener { numClicked(it) }
        textView10.setOnClickListener { numClicked(it) }
        textView11.setOnClickListener { numClicked(it) }
        textView8.setOnClickListener { numClicked(it) }
        textView.setOnClickListener { numClicked(it) }
        textView3.setOnClickListener { numClicked(it) }
        textView6.setOnClickListener { numClicked(it) }
        textView5.setOnClickListener { numClicked(it) }
        textView7.setOnClickListener { numClicked(it) }
        textView2.setOnClickListener { numClicked(it) }

        //back
        textView13.setOnClickListener { deleteClicked(it) }
        //equal
        textView22.setOnClickListener { equalClicked(it) }
    }

    //数字点击事件
    @ExperimentalStdlibApi
    fun numClicked(view: View){
        val textView=view as TextView
        currentInputNum.append(textView.text)
        if(isNumStart){
            numList.add(textView.text.toString().toInt())
            isNumStart=false
        }else{
            numList[numList.size-1]=currentInputNum.toString().toInt()
        }

        Log.v("zxc","$numList")
        calculate()
        showProcessText()
    }

    //运算符点击事件
    fun operateClicked(view: View){
        val textView=view as TextView
        operateList.add(textView.text.toString())
        isNumStart=true
        currentInputNum.clear()
        Log.v("zxc","$operateList")

        showProcessText()
    }

    //等于点击事件
    fun equalClicked(view: View){
        calculate()
        process_textview.text=""
        currentInputNum.clear()
        isNumStart=true
        numList.clear()
        operateList.clear()
    }



    //删除点击事件
    @ExperimentalStdlibApi
    fun deleteClicked(view: View){
        if (numList.isNotEmpty()){
            if (numList.size>operateList.size){
                numList.removeLast()
                currentInputNum.clear()
                isNumStart=true
            }else{
                operateList.removeLast()
                currentInputNum.append(numList.last())
            }
            if (numList.size==1){
                result_textview.text=numList[0].toString()
            }
            if (numList.size==0){
                result_textview.text="0"
            }

            calculate()
            showProcessText()
        }

    }

    //显示数据Process
    fun showProcessText(){
        val str=StringBuilder()
        for ((i,num)in numList.withIndex()){
            str.append(num)
            //拼接运算符
            if (operateList.size>i){
                str.append(" ${operateList[i]} ")
            }
        }
        process_textview.text=str.toString()
    }

    fun calculate(){
        if (numList.size>1){
            var i:Int=0
            var parm1=numList[0].toFloat()
            var parm2:Float=0F
            while (true){
                if (operateList[i]=="x"||operateList[i]=="÷"){
                    if (i+1<numList.size){
                        parm2=numList[i+1].toFloat()
                        parm1=realCalculate(parm1,parm2,operateList[i])
                    }
                }else{
                    //是加减先判断下一个运算符是否为乘除
                    if(operateList.size-1<=i||(operateList[i+1]=="+"||operateList[i+1]=="-")){
                        if (numList.size>i+1){
                            parm2=numList[i+1].toFloat()
                            parm1=realCalculate(parm1,parm2,operateList[i])
                        }else{
                            break
                        }
                    }else{
                        var j=i+1
                        var mParm1=numList[j].toFloat()
                        var mParm2=0.0f
                        while (operateList[j]=="x"||operateList[j]=="÷"){
                            if (numList.size>j+1){
                                mParm2= numList[j+1].toFloat()
                                mParm1=realCalculate(mParm1,mParm2,operateList[j])

                                j++
                                if(j==operateList.size){
                                    break
                                }
                            }else{
                                break
                            }

                        }
                        parm2=mParm1
                        parm1=realCalculate(parm1,parm2,operateList[i])
                        i=j-1
                        Log.v("zxc"," $parm1 , $parm2  $operateList[i]")
                    }
                }
                i++
                if(operateList.size == i){
                    break
                }
            }
          result_textview.text=String.format("%.2f",parm1)
        }

    }

    fun realCalculate(parm1:Float,parm2:Float,operator:String):Float{
        var result:Float= 0F
        when(operator){
            "+"->result=parm1 +parm2
            "-"->result=parm1 -parm2
            "x"->result=parm1 *parm2
            "÷"->result=parm1 /parm2
        }
        return result
    }
}