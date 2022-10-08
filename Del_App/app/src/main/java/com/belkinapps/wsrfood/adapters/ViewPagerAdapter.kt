package com.belkinapps.wsrfood

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.belkinapps.wsrfood.activities.MainActivity
import com.belkinapps.wsrfood.activities.SignInActivity
import com.belkinapps.wsrfood.activities.SignUpActivity

class ViewPagerAdapter (var isConnected: Boolean, var pref: SharedPreferences?) : RecyclerView.Adapter<PagerVH>() {

    var isLogged = false

    fun SaveState(state: Boolean) {
        val editor = pref?.edit()
        editor?.putBoolean("isLogged", state)
        editor?.apply()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerVH =
        PagerVH(LayoutInflater.from(parent.context).inflate(R.layout.on_boarding_screen_1_2, parent, false))

    override fun getItemCount(): Int = 2


    @SuppressLint("ResourceType")
    override fun onBindViewHolder(holder: PagerVH, position: Int) = holder.itemView.run {

        if(position == 0){
            holder.fpos_img.setImageResource(R.drawable.ic_ililustration_1)
            holder.delivery_text.setText(R.string.delivery)
            holder.work_text.setText(R.string.work)
        }
        if(position == 1) {
            holder.spos_img.setImageResource(R.drawable.ic_illustration)
            holder.spos_drawable.setImageResource(R.drawable.ic_rectangle_473)
            holder.welcome_text.setText(R.string.welcome)
            holder.spos_name.setText(R.string.app_name)
            holder.signin_btn.scaleX = 1F
            holder.signin_btn.setOnClickListener{
                val myIntent = Intent(context, SignInActivity::class.java)
                holder.itemView.getContext().startActivity(myIntent)
            }
            holder.signup_btn.scaleX = 1F
            holder.signup_btn.setOnClickListener{
                val myIntent = Intent(context, SignUpActivity::class.java)
                holder.itemView.getContext().startActivity(myIntent)
            }
            holder.signin_btn.setText(R.string.sign_in)
            holder.signup_btn.setText(R.string.sign_up)
            if (!isConnected) {
                holder.skip_auth.visibility = View.VISIBLE
                holder.skip_auth.setOnClickListener {
                    isLogged = true
                    SaveState(isLogged)
                    val myIntent = Intent(context, MainActivity::class.java)
                    holder.itemView.getContext().startActivity(myIntent)
                }
            }

        }
    }

}

class PagerVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val fpos_img: ImageView = itemView.findViewById(R.id.fpos_img)
    val delivery_text: TextView = itemView.findViewById(R.id.delivery_text)
    val work_text: TextView = itemView.findViewById(R.id.work_text)
    val spos_img: ImageView = itemView.findViewById(R.id.spos_img)
    val spos_drawable: ImageView = itemView.findViewById(R.id.spos_drawable)
    val welcome_text: TextView = itemView.findViewById(R.id.welcome_text)
    val spos_name: TextView = itemView.findViewById(R.id.spos_name)
    val signin_btn: Button = itemView.findViewById(R.id.signin_btn)
    val signup_btn: Button = itemView.findViewById(R.id.signup_btn)
    val skip_auth: TextView = itemView.findViewById(R.id.skip_auth)
}