package com.yourbrand.englishlearn.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import com.yourbrand.englishlearn.MainActivity
import com.yourbrand.englishlearn.services

enum class Tab { TODAY, VOCAB, GRAMMAR, EXAM, ME }

/** How the floating pet behaves on a screen (SKILL.md 5.1). */
enum class PetMode { FLOATING, MINI, HIDDEN }

/** A screen = a view + a little lifecycle. Single activity, no fragments. */
abstract class Screen(val activity: MainActivity) {
    lateinit var view: View
        private set

    /** Highlighted bottom tab; null hides the bottom bar. */
    open val tab: Tab? = null
    /** Banner only on the five hubs. */
    open val allowsBanner: Boolean = false
    open val petMode: PetMode = PetMode.HIDDEN

    protected val services get() = activity.services
    protected val inflater: LayoutInflater get() = activity.layoutInflater
    protected val ctx get() = activity

    internal fun create(parent: ViewGroup): View { view = onCreateView(parent); return view }
    internal val hasView get() = ::view.isInitialized
    protected abstract fun onCreateView(parent: ViewGroup): View

    open fun onShown(firstTime: Boolean) {}
    open fun onHidden() {}
    open fun onDestroy() {}
    open fun onPause() {}
    open fun onResume() {}
    /** True when the screen consumed the back press. */
    open fun onBack(): Boolean = false

    fun str(id: Int, vararg args: Any): String = if (args.isEmpty()) activity.getString(id) else activity.getString(id, *args)
}

class Navigator(private val container: FrameLayout, private val onChanged: (Screen) -> Unit) {
    private val stack = ArrayList<Screen>()
    val current: Screen? get() = stack.lastOrNull()
    val depth: Int get() = stack.size
    val screens: List<Screen> get() = stack

    fun push(screen: Screen) = show(screen, Transition.FORWARD) { stack += screen }

    fun reset(screen: Screen) {
        val old = stack.toList()
        show(screen, Transition.FADE) { stack.clear(); stack += screen }
        old.forEach { it.onDestroy() }
    }

    /**
     * Tab switch to a kept hub screen: reuse its views (built before) instead of creating new ones.
     * [keep] screens are not destroyed when they leave the stack.
     */
    fun resetTo(screen: Screen, keep: Collection<Screen>) {
        if (!screen.hasView) {
            val old = stack.toList()
            show(screen, Transition.FADE) { stack.clear(); stack += screen }
            old.filter { it !in keep }.forEach { it.onDestroy() }
            return
        }
        val old = stack.toList()
        val outgoing = current
        stack.clear(); stack += screen
        outgoing?.onHidden()
        animateSwap(outgoing?.view, screen.view, Transition.FADE)
        screen.onShown(false)
        onChanged(screen)
        old.filter { it !== screen && it !in keep }.forEach { it.onDestroy() }
    }

    fun replace(screen: Screen) {
        val old = current
        show(screen, Transition.FORWARD) { stack.removeLastOrNull(); stack += screen }
        old?.onDestroy()
    }

    fun pop(): Boolean {
        if (stack.size <= 1) return false
        val leaving = stack.removeAt(stack.lastIndex)
        val target = stack.last()
        animateSwap(leaving.view, target.view, Transition.BACK)
        leaving.onHidden(); leaving.onDestroy()
        target.onShown(false)
        onChanged(target)
        return true
    }

    /**
     * Rebuilds the views of every screen on the stack in place (after a per-app locale change),
     * keeping each screen's state. Only the top screen stays visible; no transition, no flash.
     */
    fun rebuildViews() {
        val top = current ?: return
        stack.forEach { screen ->
            val old = if (screen.hasView) screen.view else null
            screen.onHidden()
            val fresh = screen.create(container)
            old?.let { it.animate().cancel(); container.removeView(it) }
            container.addView(fresh, FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
            fresh.visibility = if (screen === top) View.VISIBLE else View.INVISIBLE
            screen.onShown(true)
            if (screen !== top) screen.onHidden()
        }
        onChanged(top)
    }

    /** Pops until [predicate] matches the top screen (or only the root is left). */
    fun popTo(predicate: (Screen) -> Boolean) { while (stack.size > 1 && !predicate(stack.last())) pop() }

    private fun show(screen: Screen, transition: Transition, mutate: () -> Unit) {
        val t0 = System.nanoTime()
        val outgoing = current
        val incoming = screen.create(container)
        val t1 = System.nanoTime()
        mutate()
        outgoing?.onHidden()
        animateSwap(outgoing?.view, incoming, transition)
        val t2 = System.nanoTime()
        screen.onShown(true)
        val t3 = System.nanoTime()
        onChanged(screen)
        val t4 = System.nanoTime()
        Perf.watchFrames(screen.javaClass.simpleName)
        Perf.log("show ${screen.javaClass.simpleName}: create=${Perf.ms(t0, t1)} swap=${Perf.ms(t1, t2)} build=${Perf.ms(t2, t3)} chrome=${Perf.ms(t3, t4)}")
    }

    /** Tab switch: fade-through 200 ms; push: shared-axis slide 24dp + fade 250 ms (SKILL.md 10). */
    private fun animateSwap(outView: View?, inView: View, transition: Transition) {
        if (inView.parent == null) container.addView(inView, FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
        val reduce = container.context.reduceMotion
        inView.animate().cancel()
        val dx = container.context.dp(24f)
        inView.alpha = 0f
        inView.translationX = if (reduce) 0f else when (transition) { Transition.FORWARD -> dx; Transition.BACK -> -dx; Transition.FADE -> 0f }
        inView.animate().alpha(1f).translationX(0f).setDuration(if (reduce) 100 else if (transition == Transition.FADE) 200 else 250)
            .setInterpolator(DecelerateInterpolator(1.4f)).withLayer().start()
        if (outView != null && outView !== inView) {
            outView.animate().cancel()
            outView.animate().alpha(0f)
                .translationX(if (reduce) 0f else when (transition) { Transition.FORWARD -> -dx / 2; Transition.BACK -> dx / 2; Transition.FADE -> 0f })
                .setDuration(if (reduce) 80 else 160).withLayer()
                .withEndAction { if (stack.none { it.view === outView }) container.removeView(outView) else outView.visibility = View.INVISIBLE; outView.alpha = 1f; outView.translationX = 0f }
                .start()
        }
        inView.visibility = View.VISIBLE
    }

    private enum class Transition { FORWARD, BACK, FADE }
}

/** Shared service shortcut for views. */
val View.svc get() = context.services
