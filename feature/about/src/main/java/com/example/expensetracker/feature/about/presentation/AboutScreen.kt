package com.example.expensetracker.feature.about.presentation

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.expensetracker.core.analytics.AnalyticsService
import com.example.expensetracker.core.remoteconfig.RemoteConfigService
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView

@Composable
fun AboutScreen(
    analyticsService: AnalyticsService? = null,
    remoteConfigService: RemoteConfigService? = null
) {
    val context = LocalContext.current
    val kemguPoint = Point(55.355057, 86.077665) // КемГУ, Кемерово, ул. Красная, 6

    val aboutDescription = remember {
        remoteConfigService?.getString("about_description")?.takeIf { it.isNotEmpty() }
            ?: "Наша компания «ExpenseTracker Team» базируется в КемГУ. Мы разрабатываем современное приложение для управления вашими расходами."
    }

    LaunchedEffect(Unit) {
        analyticsService?.trackEvent("screen_viewed", mapOf("screen_name" to "About"))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Заголовок и описание
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surfaceVariant,
            tonalElevation = 4.dp
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "О нас",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = aboutDescription,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Адрес: г. Кемерово, ул. Красная, 6",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Кнопка маршрута
        Button(
            onClick = {
                analyticsService?.trackEvent("build_route_clicked", mapOf("destination" to "KemGU"))
                val uri = Uri.parse("yandexmaps://maps.yandex.ru/?rtext=~${kemguPoint.latitude},${kemguPoint.longitude}&rtt=auto")
                val intent = Intent(Intent.ACTION_VIEW, uri)
                try {
                    context.startActivity(intent)
                } catch (e: Exception) {
                    val browserUri = Uri.parse("https://yandex.ru/maps/?rtext=~${kemguPoint.latitude},${kemguPoint.longitude}&rtt=auto")
                    context.startActivity(Intent(Intent.ACTION_VIEW, browserUri))
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text("Построить маршрут до офиса")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(16.dp)
        ) {
            YandexMap(
                modifier = Modifier.fillMaxSize(),
                point = kemguPoint
            )
        }
    }
}

@Composable
fun YandexMap(
    modifier: Modifier = Modifier,
    point: Point
) {
    val context = LocalContext.current
    val mapView = remember { MapView(context) }
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> {
                    MapKitFactory.getInstance().onStart()
                    mapView.onStart()
                }
                Lifecycle.Event.ON_STOP -> {
                    mapView.onStop()
                    MapKitFactory.getInstance().onStop()
                }
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    AndroidView(
        modifier = modifier,
        factory = { mapView },
        update = { view ->
            view.mapWindow.map.move(
                CameraPosition(point, 16.0f, 0.0f, 0.0f)
            )
            view.mapWindow.map.mapObjects.clear()
            view.mapWindow.map.mapObjects.addPlacemark().apply {
                geometry = point
            }
        }
    )
}
