package jp.techacademy.akiko.sugiyama.apiapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.squareup.picasso.Picasso
import android.view.View
import kotlinx.android.synthetic.main.activity_web_view.*
import kotlinx.android.synthetic.main.recycler_favorite.*
import java.io.Serializable

class WebViewActivity: AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        val shop = intent.getSerializableExtra(KEY_SHOP)
        if(shop is Shop) {
            webView.loadUrl((if (shop.couponUrls.sp.isNotEmpty()) shop.couponUrls.sp else shop.couponUrls.pc).toString())
            val isFavorite = FavoriteShop.findBy(shop.id) != null
            if (isFavorite) couponSiteText.text = "お気に入りから削除"
            else{
                couponSiteText.text = "お気に入りに登録"
                favoriteButton.setImageResource(R.drawable.ic_star_border)
            }
        }else if(shop is FavoriteShop){
            webView.loadUrl( shop.url)
            couponSiteText.text = "お気に入りから削除"
        }

        favoriteButton.setOnClickListener(this)
    }

    companion object {
        private const val KEY_SHOP = "key_shop"
        fun start(activity: Activity, shop: Shop) {
            activity.startActivity(Intent(activity, WebViewActivity::class.java).putExtra(KEY_SHOP, shop))
        }
        fun startFavorite(activity: Activity, favoriteShop: FavoriteShop) {
            activity.startActivity(Intent(activity, WebViewActivity::class.java).putExtra(KEY_SHOP, favoriteShop))
        }
    }

    override fun onClick(v: View) {
        val shop = intent.getSerializableExtra(KEY_SHOP)
        if(shop is Shop) {
            val isFavorite = FavoriteShop.findBy(shop.id) != null
            if (isFavorite) {
                FavoriteShop.delete(shop.id)
                couponSiteText.text = "お気に入りに登録"
                favoriteButton.setImageResource(R.drawable.ic_star_border)
            } else {
                FavoriteShop.insert(FavoriteShop().apply {
                    id = shop.id
                    name = shop.name
                    address = shop.address
                    imageUrl = shop.logoImage
                    url = if (shop.couponUrls.sp.isNotEmpty()) shop.couponUrls.sp else shop.couponUrls.pc
                })
                couponSiteText.text = "お気に入りから削除"
                favoriteButton.setImageResource(R.drawable.ic_star)
            }
        }else if(shop is FavoriteShop) {
            val isFavorite = FavoriteShop.findBy(shop.id) != null
            if (isFavorite) {
                FavoriteShop.delete(shop.id)
                couponSiteText.text = "お気に入りに登録"
                favoriteButton.setImageResource(R.drawable.ic_star_border)
            } else {
                FavoriteShop.insert(FavoriteShop().apply {
                    id = shop.id
                    name = shop.name
                    address = shop.address
                    imageUrl = shop.imageUrl
                    url = shop.url
                })
                couponSiteText.text = "お気に入りから削除"
                favoriteButton.setImageResource(R.drawable.ic_star)
            }
        }
    }
}