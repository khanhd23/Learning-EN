package com.yourbrand.englishlearn.screens

import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import android.widget.TextView
import com.yourbrand.englishlearn.MainActivity
import com.yourbrand.englishlearn.R
import com.yourbrand.englishlearn.learning.PracticeBuilder
import com.yourbrand.englishlearn.learning.PracticeBuilder.Filters
import com.yourbrand.englishlearn.learning.PracticeBuilder.Source
import com.yourbrand.englishlearn.learning.PracticeBuilder.Type
import com.yourbrand.englishlearn.ui.*
import org.json.JSONArray
import org.json.JSONObject

/** S12 — "Tạo bài luyện riêng": one form with a live match count at the bottom. */
class BuilderScreen(activity: MainActivity) : ScrollScreen(activity) {
    override val petMode = PetMode.HIDDEN
    override val barTitle: String get() = str(R.string.builder_title)
    private val builder by lazy { PracticeBuilder(services.content) }

    private var types = mutableSetOf(Type.VOCAB, Type.GRAMMAR)
    private var topics = mutableSetOf<String>()
    private var skills = mutableSetOf<String>()
    private var range = 0 // 0 auto · 1 = 1–2 · 2 = 2–3 · 3 = 3–4 · 4 = 4–5 · 5 = all
    private var source = Source.ALL
    private var count = 20
    private var timed = false
    private lateinit var countText: TextView
    private lateinit var startBtn: TextView
    private var lastResult: PracticeBuilder.Result? = null
    private val debounce = Runnable { recount() }

    override fun onCreateView(parent: android.view.ViewGroup): View {
        val v = super.onCreateView(parent)
        val c = ctx
        // Sticky footer with the live count + start
        val f = Kit.vbox(c) {
            setPadding(c.dpi(16), c.dpi(10), c.dpi(16), c.dpi(14))
            setBackgroundColor(c.col(R.color.surface))
            elevation = c.dp(8f)
        }
        countText = Kit.text(c, "", R.style.Text_BodyStrong)
        f.addView(countText)
        val r = Kit.hbox(c).margins(c, top = 8)
        r.addView(Kit.secondary(c, str(R.string.save_preset), 0) { savePreset() }.apply { layoutParams = lp(0, c.dpi(52), 1f) })
        startBtn = Kit.primary(c, str(R.string.start), 0) { start() }.apply { layoutParams = lp(0, c.dpi(52), 1.4f).apply { marginStart = c.dpi(10) } }
        r.addView(startBtn)
        f.addView(r)
        root.addView(f)
        return v
    }

    private fun filters(): Filters {
        val lvl = services.settings.userLevel
        val (mn, mx) = when (range) { 0 -> (lvl - 1).coerceAtLeast(1) to (lvl + 1).coerceAtMost(5); 1 -> 1 to 2; 2 -> 2 to 3; 3 -> 3 to 4; 4 -> 4 to 5; else -> 1 to 5 }
        return Filters(types.toSet(), topics.toSet(), skills.toSet(), mn, mx, source)
    }

    private fun scheduleRecount() { body.removeCallbacks(debounce); body.postDelayed(debounce, 150) }

    private fun recount() {
        val r = builder.query(filters()) { services.store.item(it) }
        lastResult = r
        countText.text = str(R.string.matches, r.matchCount)
        startBtn.isEnabled = r.matchCount >= 1
        loosen?.show(r.matchCount < 5)
    }

    private var loosen: View? = null

    override fun build(body: LinearLayout) {
        val c = ctx
        // Presets
        val presets = services.settings.builderPresets
        if (presets.length() > 0) {
            val row = Kit.hbox(c)
            for (i in 0 until presets.length()) {
                val p = presets.getJSONObject(i)
                row.addView(Kit.chip(c, "⭐ " + p.optString("name"), false, c.col(R.color.accent)) { loadPreset(p); refresh() })
            }
            body.addView(Kit.hscroll(c, row).margins(c, top = 4))
        }

        section(body, R.string.b_type)
        multi(body, listOf(Type.PART5 to R.string.t_part5, Type.PART6 to R.string.t_part6, Type.VOCAB to R.string.t_vocab, Type.GRAMMAR to R.string.t_grammar, Type.SCHOOL to R.string.t_school), types)

        section(body, R.string.b_topic)
        val tflow = Kit.flow(c)
        services.content.topics.forEach { t ->
            tflow.addView(Kit.chip(c, t.icon + " " + t.name, t.id in topics, Hues.color(c, t.hue)) { v ->
                if (!topics.add(t.id)) topics.remove(t.id)
                Kit.styleChip(v, t.id in topics, Hues.color(c, t.hue)); scheduleRecount()
            })
        }
        body.addView(tflow)

        section(body, R.string.b_skill)
        val sflow = Kit.flow(c)
        builder.skillTags.forEach { s ->
            sflow.addView(Kit.chip(c, TagNames.label(c, s), s in skills) { v -> if (!skills.add(s)) skills.remove(s); Kit.styleChip(v, s in skills); scheduleRecount() })
        }
        body.addView(sflow)

        section(body, R.string.b_level)
        single(body, listOf(R.string.lv_auto, R.string.lv_12, R.string.lv_23, R.string.lv_34, R.string.lv_45, R.string.f_all), range) { range = it }

        section(body, R.string.b_source)
        single(body, listOf(R.string.src_new, R.string.src_wrong, R.string.src_saved, R.string.f_all), Source.entries.indexOf(source)) { source = Source.entries[it] }

        section(body, R.string.b_count)
        single(body, listOf(10, 20, 30, 50).map { it.toString() }, listOf(10, 20, 30, 50).indexOf(count), raw = true) { count = listOf(10, 20, 30, 50)[it] }

        section(body, R.string.b_mode)
        single(body, listOf(R.string.mode_practice, R.string.mode_timed), if (timed) 1 else 0) { timed = it == 1 }

        loosen = Kit.card(c, 14, 16, c.col(R.color.warning_container), null) {
            addView(Kit.text(c, str(R.string.too_few), R.style.Text_Body))
            addView(Kit.secondary(c, str(R.string.loosen)) { topics.clear(); skills.clear(); range = 5; source = Source.ALL; types.addAll(Type.entries); refresh() })
        }.also { it.visibility = View.GONE; body.addView(it) }
        body.addView(Kit.spacer(c, 140))
        body.post { recount() }
    }

    private fun section(body: LinearLayout, res: Int) = body.addView(Kit.text(ctx, str(res), R.style.Text_BodyStrong).margins(ctx, top = 18, bottom = 6))

    private fun multi(body: LinearLayout, items: List<Pair<Type, Int>>, set: MutableSet<Type>) {
        val flow = Kit.flow(ctx)
        items.forEach { (t, l) -> flow.addView(Kit.chip(ctx, str(l), t in set) { v -> if (!set.add(t)) { if (set.size > 1) set.remove(t) }; Kit.styleChip(v, t in set); scheduleRecount() }) }
        body.addView(flow)
    }

    private fun single(body: LinearLayout, labels: List<Any>, selected: Int, raw: Boolean = false, onPick: (Int) -> Unit) {
        val flow = Kit.flow(ctx)
        val views = ArrayList<TextView>()
        labels.forEachIndexed { i, l ->
            val text = if (raw || l is String) l.toString() else str(l as Int)
            views += Kit.chip(ctx, text, i == selected) { _ ->
                onPick(i)
                views.forEachIndexed { j, v -> Kit.styleChip(v, j == i) }
                scheduleRecount()
            }.also { flow.addView(it) }
        }
        body.addView(flow)
    }

    private fun start() {
        val r = lastResult ?: return
        activity.startSession(services.builder().builder(str(R.string.builder_title), r, count, timed))
    }

    private fun savePreset() {
        val c = ctx
        Dialogs.custom(c) { box, d ->
            box.addView(Kit.text(c, str(R.string.save_preset), R.style.Text_Title))
            val input = android.widget.EditText(c).apply { hint = str(R.string.preset_name); setSingleLine(); layoutParams = lp().apply { topMargin = c.dpi(12) } }
            box.addView(input)
            box.addView(Kit.primary(c, str(R.string.save)) {
                val name = input.text.toString().ifBlank { str(R.string.preset_default) }
                val arr = services.settings.builderPresets
                val list = (0 until arr.length()).map { arr.getJSONObject(it) }.toMutableList()
                list.add(0, JSONObject().put("name", name).put("types", JSONArray(types.map { it.name })).put("topics", JSONArray(topics.toList()))
                    .put("skills", JSONArray(skills.toList())).put("range", range).put("source", source.name).put("count", count).put("timed", timed))
                services.settings.builderPresets = JSONArray(list.take(5))
                d.dismiss(); activity.toast(str(R.string.preset_saved)); refresh()
            })
        }
    }

    private fun loadPreset(p: JSONObject) {
        fun arr(k: String) = p.optJSONArray(k)?.let { a -> List(a.length()) { a.getString(it) } }.orEmpty()
        types = arr("types").mapNotNull { runCatching { Type.valueOf(it) }.getOrNull() }.toMutableSet().ifEmpty { mutableSetOf(Type.VOCAB) }
        topics = arr("topics").toMutableSet(); skills = arr("skills").toMutableSet()
        range = p.optInt("range"); source = runCatching { Source.valueOf(p.optString("source")) }.getOrDefault(Source.ALL)
        count = p.optInt("count", 20); timed = p.optBoolean("timed")
    }
}
