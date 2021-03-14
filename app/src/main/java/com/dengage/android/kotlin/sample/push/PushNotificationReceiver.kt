package com.dengage.android.kotlin.sample.push

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.dengage.android.kotlin.sample.R
import com.dengage.sdk.NotificationReceiver
import com.dengage.sdk.Utils
import com.dengage.sdk.models.Message

/**
 * Created by Batuhan Coskun on 19 December 2020
 */
class PushNotificationReceiver : NotificationReceiver() {

    override fun onCarouselRender(context: Context?, intent: Intent?, message: Message?) {
        super.onCarouselRender(context, intent, message)

        val items = message?.carouselContent
        if (items.isNullOrEmpty() || intent == null) return
        val size = items.size
        val current = 0
        val left = (current - 1 + size) % size
        val right = (current + 1) % size

        val itemTitle = items[current].title
        val itemDesc = items[current].description

        // set intets (right button, left button, item click)
        val itemIntent = getItemClickIntent(intent.extras, context?.packageName)
        val leftIntent = getLeftItemIntent(intent.extras, context?.packageName)
        val rightIntent = getRightItemIntent(intent.extras, context?.packageName)
        val deleteIntent = getDeleteIntent(intent.extras, context?.packageName)
        val contentIntent = getContentIntent(intent.extras, context?.packageName)
        val carouseItemIntent = PendingIntent.getBroadcast(
            context, 0,
            itemIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )
        val carouselLeftIntent = PendingIntent.getBroadcast(
            context, 1,
            leftIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )
        val carouselRightIntent = PendingIntent.getBroadcast(
            context, 2,
            rightIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )
        val deletePendingIntent = PendingIntent.getBroadcast(
            context, 4,
            deleteIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )
        val contentPendingIntent = PendingIntent.getBroadcast(
            context, 5,
            contentIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )

        // set views for the layout
        val collapsedView = RemoteViews(
            context?.packageName,
            R.layout.den_carousel_collapsed
        )
        collapsedView.setTextViewText(R.id.den_carousel_title, message.title)
        collapsedView.setTextViewText(R.id.den_carousel_message, message.message)
        val carouselView = RemoteViews(
            context?.packageName,
            R.layout.den_carousel_portrait
        )
        carouselView.setTextViewText(R.id.den_carousel_title, message.title)
        carouselView.setTextViewText(R.id.den_carousel_message, message.message)
        carouselView.setTextViewText(R.id.den_carousel_item_title, itemTitle)
        carouselView.setTextViewText(R.id.den_carousel_item_description, itemDesc)

        Utils.loadCarouselImageToView(
            carouselView,
            R.id.den_carousel_portrait_left_image,
            items[left]
        )
        Utils.loadCarouselImageToView(
            carouselView,
            R.id.den_carousel_portrait_current_image,
            items[current]
        )
        Utils.loadCarouselImageToView(
            carouselView,
            R.id.den_carousel_portrait_right_image,
            items[right]
        )

        carouselView.setOnClickPendingIntent(R.id.den_carousel_left_arrow, carouselLeftIntent)
        carouselView.setOnClickPendingIntent(
            R.id.den_carousel_portrait_current_image,
            carouseItemIntent
        )
        carouselView.setOnClickPendingIntent(R.id.den_carousel_item_title, carouseItemIntent)
        carouselView.setOnClickPendingIntent(R.id.den_carousel_item_description, carouseItemIntent)
        carouselView.setOnClickPendingIntent(R.id.den_carousel_right_arrow, carouselRightIntent)
        // create channelId
        var channelId: String? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = notificationChannel
            createNotificationChannel(context, notificationChannel)
            channelId = notificationChannel.id
        }

        if (context != null) {
            // set views for the notification
            val notification = NotificationCompat.Builder(context, channelId ?: "1")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setCustomContentView(collapsedView)
                .setCustomBigContentView(carouselView)
                .setContentIntent(contentPendingIntent)
                .setDeleteIntent(deletePendingIntent)
                .build()
            // show message
            val notificationManager = NotificationManagerCompat.from(context)
            notificationManager.notify(
                message.messageSource,
                message.messageId,
                notification
            )
        }
    }

    override fun onCarouselReRender(context: Context?, intent: Intent?, message: Message?) {
        super.onCarouselReRender(context, intent, message)

        val items = message?.carouselContent
        if (items.isNullOrEmpty() || intent == null) return
        val bundle = intent.extras
        val prevIndex = bundle?.getInt("current")
        val navigation = bundle?.getString("navigation", "right")
        val size = items.size
        val current = if (navigation.equals("right")) {
            ((prevIndex ?: 0) + 1) % size
        } else {
            ((prevIndex ?: 0) - 1 + size) % size
        }
        val right = (current + 1) % size
        val left = (current - 1 + size) % size
        intent.putExtra("current", current)

        val itemTitle = items[current].title
        val itemDesc = items[current].description

        // set intents (next button, rigth button and item click)
        val itemIntent = getItemClickIntent(intent.extras, context?.packageName)
        val leftIntent = getLeftItemIntent(intent.extras, context?.packageName)
        val rightIntent = getRightItemIntent(intent.extras, context?.packageName)
        val deleteIntent = getDeleteIntent(intent.extras, context?.packageName)
        val contentIntent = getContentIntent(intent.extras, context?.packageName)
        val carouseItemIntent = PendingIntent.getBroadcast(
            context, 0,
            itemIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )
        val carouselLeftIntent = PendingIntent.getBroadcast(
            context, 1,
            leftIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )
        val carouselRightIntent = PendingIntent.getBroadcast(
            context, 2,
            rightIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )
        val deletePendingIntent = PendingIntent.getBroadcast(
            context, 4,
            deleteIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )
        val contentPendingIntent = PendingIntent.getBroadcast(
            context, 5,
            contentIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )

        // set views for the layout
        val collapsedView = RemoteViews(
            context?.packageName,
            R.layout.den_carousel_collapsed
        )
        collapsedView.setTextViewText(R.id.den_carousel_title, message.title)
        collapsedView.setTextViewText(R.id.den_carousel_message, message.message)
        val carouselView = RemoteViews(
            context?.packageName,
            R.layout.den_carousel_portrait
        )
        carouselView.setTextViewText(R.id.den_carousel_title, message.title)
        carouselView.setTextViewText(R.id.den_carousel_message, message.message)
        carouselView.setTextViewText(R.id.den_carousel_item_title, itemTitle)
        carouselView.setTextViewText(R.id.den_carousel_item_description, itemDesc)

        Utils.loadCarouselImageToView(
            carouselView,
            R.id.den_carousel_portrait_left_image,
            items[left]
        )
        Utils.loadCarouselImageToView(
            carouselView,
            R.id.den_carousel_portrait_current_image,
            items[current]
        )
        Utils.loadCarouselImageToView(
            carouselView,
            R.id.den_carousel_portrait_right_image,
            items[right]
        )

        carouselView.setOnClickPendingIntent(R.id.den_carousel_left_arrow, carouselLeftIntent)
        carouselView.setOnClickPendingIntent(
            R.id.den_carousel_portrait_current_image,
            carouseItemIntent
        )
        carouselView.setOnClickPendingIntent(R.id.den_carousel_item_title, carouseItemIntent)
        carouselView.setOnClickPendingIntent(R.id.den_carousel_item_description, carouseItemIntent)
        carouselView.setOnClickPendingIntent(R.id.den_carousel_right_arrow, carouselRightIntent)
        // create a channel id.
        var channelId: String? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = notificationChannel
            createNotificationChannel(context, notificationChannel)
            channelId = notificationChannel.id
        }

        if (context != null) {
            // set your views for the notification
            val notification = NotificationCompat.Builder(context, channelId ?: "1")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setCustomContentView(collapsedView)
                .setCustomBigContentView(carouselView)
                .setContentIntent(contentPendingIntent)
                .setDeleteIntent(deletePendingIntent)
                .build()
            // show message again silently with next,prev and current item.
            notification.flags = Notification.FLAG_AUTO_CANCEL or Notification.FLAG_ONLY_ALERT_ONCE
            val notificationManager = NotificationManagerCompat.from(context);
            notificationManager.notify(
                message.messageSource,
                message.messageId,
                notification
            )
        }
    }

}