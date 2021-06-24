package com.dengage.android.kotlin.sample.push

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.media.AudioAttributes
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.dengage.android.kotlin.sample.R
import com.dengage.sdk.Constants
import com.dengage.sdk.NotificationReceiver
import com.dengage.sdk.Utils
import com.dengage.sdk.callback.DengageCallback
import com.dengage.sdk.models.DengageError
import com.dengage.sdk.models.Message
import java.util.*

/**
 * Created by Batuhan Coskun on 19 December 2020
 */
class PushNotificationReceiver : NotificationReceiver() {

    override fun onCarouselRender(context: Context, intent: Intent?, message: Message?) {
        super.onCarouselRender(context, intent, message)

        val items = message?.carouselContent
        if (items.isNullOrEmpty() || intent == null) return
        val size = items.size
        val current = 0
        val left = (current - 1 + size) % size
        val right = (current + 1) % size

        val itemTitle = items[current].title
        val itemDesc = items[current].description

        // set intents (right button, left button, item click)
        val itemIntent = getItemClickIntent(intent.extras, context.packageName)
        val leftIntent = getLeftItemIntent(intent.extras, context.packageName)
        val rightIntent = getRightItemIntent(intent.extras, context.packageName)
        val deleteIntent = getDeleteIntent(intent.extras, context.packageName)
        val contentIntent = getContentIntent(intent.extras, context.packageName)

        val carouseItemIntent = getPendingIntent(context, 0, itemIntent)
        val carouselLeftIntent = getPendingIntent(context, 1, leftIntent)
        val carouselRightIntent = getPendingIntent(context, 2, rightIntent)
        val deletePendingIntent = getPendingIntent(context, 4, deleteIntent)
        val contentPendingIntent = getPendingIntent(context, 5, contentIntent)

        // set views for the layout
        val collapsedView = RemoteViews(
            context.packageName,
            R.layout.den_carousel_collapsed
        )
        collapsedView.setTextViewText(R.id.den_carousel_title, message.title)
        collapsedView.setTextViewText(R.id.den_carousel_message, message.message)
        val carouselView = RemoteViews(
            context.packageName,
            R.layout.den_carousel_portrait
        )
        carouselView.setTextViewText(R.id.den_carousel_title, message.title)
        carouselView.setTextViewText(R.id.den_carousel_message, message.message)
        carouselView.setTextViewText(R.id.den_carousel_item_title, itemTitle)
        carouselView.setTextViewText(R.id.den_carousel_item_description, itemDesc)

        carouselView.setOnClickPendingIntent(R.id.den_carousel_left_arrow, carouselLeftIntent)
        carouselView.setOnClickPendingIntent(
            R.id.den_carousel_portrait_current_image,
            carouseItemIntent
        )
        carouselView.setOnClickPendingIntent(R.id.den_carousel_item_title, carouseItemIntent)
        carouselView.setOnClickPendingIntent(R.id.den_carousel_item_description, carouseItemIntent)
        carouselView.setOnClickPendingIntent(R.id.den_carousel_right_arrow, carouselRightIntent)

        val channelId = createNotificationChannel(context, message)

        // set views for the notification
        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setCustomContentView(collapsedView)
            .setCustomBigContentView(carouselView)
            .setContentIntent(contentPendingIntent)
            .setDeleteIntent(deletePendingIntent)
            .build()


        // --------- Behavior-1 ---------
        /*Utils.loadCarouselImageToView(
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

        // show message
        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(
            message.messageSource,
            message.messageId,
            notification
        )*/
        // --------- Behavior-1 ---------


        // You can use alternate behavior, if carousel image download bug occurs
        // Comment Behavior-1 and uncomment Behavior-2
        // --------- Behavior-2 ---------
        Utils.loadCarouselContents(
            message.carouselContent,
            object : DengageCallback<Array<Bitmap>> {
                override fun onError(error: DengageError) {
                    Toast.makeText(context, error.errorMessage ?: "", Toast.LENGTH_LONG).show()
                    Log.e("NotificationReceiver", error.errorMessage ?: "")
                }

                override fun onResult(result: Array<Bitmap>) {
                    carouselView.setImageViewBitmap(
                        R.id.den_carousel_portrait_left_image,
                        result[left]
                    )
                    carouselView.setImageViewBitmap(
                        R.id.den_carousel_portrait_current_image,
                        result[current]
                    )
                    carouselView.setImageViewBitmap(
                        R.id.den_carousel_portrait_right_image,
                        result[right]
                    )

                    // show message
                    val notificationManager = NotificationManagerCompat.from(context)
                    notificationManager.notify(
                        message.messageSource,
                        message.messageId,
                        notification
                    )
                }
            })
        // --------- Behavior-2 ---------

    }

    override fun onCarouselReRender(context: Context, intent: Intent?, message: Message?) {
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
        val itemIntent = getItemClickIntent(intent.extras, context.packageName)
        val leftIntent = getLeftItemIntent(intent.extras, context.packageName)
        val rightIntent = getRightItemIntent(intent.extras, context.packageName)
        val deleteIntent = getDeleteIntent(intent.extras, context.packageName)
        val contentIntent = getContentIntent(intent.extras, context.packageName)

        val carouseItemIntent = getPendingIntent(context, 0, itemIntent)
        val carouselLeftIntent = getPendingIntent(context, 1, leftIntent)
        val carouselRightIntent = getPendingIntent(context, 2, rightIntent)
        val deletePendingIntent = getPendingIntent(context, 4, deleteIntent)
        val contentPendingIntent = getPendingIntent(context, 5, contentIntent)

        // set views for the layout
        val collapsedView = RemoteViews(
            context.packageName,
            R.layout.den_carousel_collapsed
        )
        collapsedView.setTextViewText(R.id.den_carousel_title, message.title)
        collapsedView.setTextViewText(R.id.den_carousel_message, message.message)
        val carouselView = RemoteViews(
            context.packageName,
            R.layout.den_carousel_portrait
        )
        carouselView.setTextViewText(R.id.den_carousel_title, message.title)
        carouselView.setTextViewText(R.id.den_carousel_message, message.message)
        carouselView.setTextViewText(R.id.den_carousel_item_title, itemTitle)
        carouselView.setTextViewText(R.id.den_carousel_item_description, itemDesc)

        carouselView.setOnClickPendingIntent(R.id.den_carousel_left_arrow, carouselLeftIntent)
        carouselView.setOnClickPendingIntent(
            R.id.den_carousel_portrait_current_image,
            carouseItemIntent
        )
        carouselView.setOnClickPendingIntent(R.id.den_carousel_item_title, carouseItemIntent)
        carouselView.setOnClickPendingIntent(R.id.den_carousel_item_description, carouseItemIntent)
        carouselView.setOnClickPendingIntent(R.id.den_carousel_right_arrow, carouselRightIntent)

        val channelId = createNotificationChannel(context, message)

        // set your views for the notification
        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setCustomContentView(collapsedView)
            .setCustomBigContentView(carouselView)
            .setContentIntent(contentPendingIntent)
            .setDeleteIntent(deletePendingIntent)
            .build()
        // show message again silently with next,prev and current item.
        notification.flags = Notification.FLAG_AUTO_CANCEL or Notification.FLAG_ONLY_ALERT_ONCE


        // --------- Behavior-1 ---------
        /*Utils.loadCarouselImageToView(
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

        // show message
        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(
            message.messageSource,
            message.messageId,
            notification
        )*/
        // --------- Behavior-1 ---------


        // You can use alternate behavior, if carousel image download bug occurs
        // Comment Behavior-1 and uncomment Behavior-2
        // --------- Behavior-2 ---------
        Utils.loadCarouselContents(
            message.carouselContent,
            object : DengageCallback<Array<Bitmap>> {
                override fun onError(error: DengageError) {
                    Toast.makeText(context, error.errorMessage ?: "", Toast.LENGTH_LONG).show()
                    Log.e("NotificationReceiver", error.errorMessage ?: "")
                }

                override fun onResult(result: Array<Bitmap>) {
                    carouselView.setImageViewBitmap(
                        R.id.den_carousel_portrait_left_image,
                        result[left]
                    )
                    carouselView.setImageViewBitmap(
                        R.id.den_carousel_portrait_current_image,
                        result[current]
                    )
                    carouselView.setImageViewBitmap(
                        R.id.den_carousel_portrait_right_image,
                        result[right]
                    )

                    // show message
                    val notificationManager = NotificationManagerCompat.from(context)
                    notificationManager.notify(
                        message.messageSource,
                        message.messageId,
                        notification
                    )
                }
            })
        // --------- Behavior-2 ---------
    }

    private fun createNotificationChannel(context: Context?, message: Message?): String {
        // generate new channel id for different sounds
        val soundUri = Utils.getSound(context, message?.sound)
        val channelId = UUID.randomUUID().toString()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager =
                context!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            // delete old notification channels
            val channels = notificationManager.notificationChannels
            if (channels != null && channels.size > 0) {
                for (channel in channels) {
                    notificationManager.deleteNotificationChannel(channel.id)
                }
            }
            val notificationChannel = NotificationChannel(
                channelId,
                Constants.CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val audioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build()
            notificationChannel.setSound(soundUri, audioAttributes)
            notificationManager.createNotificationChannel(notificationChannel)
        }
        return channelId
    }

    private fun getPendingIntent(
        context: Context,
        requestCode: Int,
        intent: Intent
    ): PendingIntent {
        return PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            } else {
                PendingIntent.FLAG_UPDATE_CURRENT
            }
        )
    }

}