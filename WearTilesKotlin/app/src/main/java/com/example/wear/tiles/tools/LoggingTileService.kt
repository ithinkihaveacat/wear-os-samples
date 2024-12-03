package com.example.wear.tiles.tools

import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.annotation.CallSuper
import androidx.concurrent.futures.CallbackToFutureAdapter
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ServiceLifecycleDispatcher
import androidx.lifecycle.lifecycleScope
import androidx.wear.protolayout.ResourceBuilders.Resources
import androidx.wear.tiles.RequestBuilders.ResourcesRequest
import androidx.wear.tiles.RequestBuilders.TileRequest
import androidx.wear.tiles.TileBuilders.Tile
import androidx.wear.tiles.TileService
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch

// A copy of SuspendingTileService with additional logging added

public abstract class LoggingTileService : TileService(), LifecycleOwner {

    @Suppress("LeakingThis")
    private val mDispatcher = ServiceLifecycleDispatcher(this)

    final override fun onTileRequest(
        requestParams: TileRequest,
    ): ListenableFuture<Tile> {
        Log.d("qqqqqq", "onTileRequest()")
        return CallbackToFutureAdapter.getFuture { completer ->
            val job = lifecycleScope.launch {
                try {
                    completer.set(tileRequest(requestParams))
                } catch (e: CancellationException) {
                    completer.setCancelled()
                } catch (e: Exception) {
                    completer.setException(e)
                }
            }

            completer.addCancellationListener({ job.cancel() }, Runnable::run)

            "Tile Request"
        }
    }

    /**
     * See [onTileRequest] for most details.
     *
     * This runs a suspending function inside the lifecycleScope
     * of the service on the Main thread.
     */
    public abstract suspend fun tileRequest(requestParams: TileRequest): Tile

    final override fun onTileResourcesRequest(
        requestParams: ResourcesRequest,
    ): ListenableFuture<Resources> {
        Log.d("qqqqqq", "onTileResourcesRequest()")

        return CallbackToFutureAdapter.getFuture { completer ->
            val job = lifecycleScope.launch {
                this.ensureActive()

                try {
                    completer.set(resourcesRequest(requestParams))
                } catch (e: CancellationException) {
                    completer.setCancelled()
                } catch (e: Exception) {
                    completer.setException(e)
                }
            }

            completer.addCancellationListener({
                job.cancel()
            }, Runnable::run)

            "Resource Request"
        }
    }

    /**
     * See [onResourcesRequest] for most details.
     *
     * This runs a suspending function inside the lifecycleScope
     * of the service on the Main thread.
     */
    public abstract suspend fun resourcesRequest(requestParams: ResourcesRequest): Resources

    @CallSuper
    override fun onCreate() {
        Log.d("qqqqqq", "onCreate()")
        mDispatcher.onServicePreSuperOnCreate()
        super.onCreate()
    }

    @CallSuper
    override fun onBind(intent: Intent): IBinder? {
        Log.d("qqqqqq", "onBind()")
        mDispatcher.onServicePreSuperOnBind()
        return super.onBind(intent)
    }

    @Deprecated("Use onStartCommand")
    final override fun onStart(intent: Intent?, startId: Int) {
        mDispatcher.onServicePreSuperOnStart()
        @Suppress("DEPRECATION")
        super.onStart(intent, startId)
    }

    // this method is added only to annotate it with @CallSuper.
    // In usual service super.onStartCommand is no-op, but in LifecycleService
    // it results in mDispatcher.onServicePreSuperOnStart() call, because
    // super.onStartCommand calls onStart().
    final override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

    @CallSuper
    override fun onDestroy() {
        Log.d("qqqqqq", "onDestroy()")
        mDispatcher.onServicePreSuperOnDestroy()
        super.onDestroy()
    }

    override val lifecycle: Lifecycle
        get() = mDispatcher.lifecycle
}

