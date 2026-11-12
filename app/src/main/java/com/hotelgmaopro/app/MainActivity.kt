package com.hotelgmaopro.app

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

val TechDarkBg = Color(0xFF0F172A)
val TechCardBg = Color(0xFF1E293B)
val TechAccent = Color(0xFFF59E0B)
val TechBlue = Color(0xFF38BDF8)
val TechGreen = Color(0xFF10B981)
val TechRed = Color(0xFFEF4444)
val TechTextPrimary = Color(0xFFF8FAFC)
val TechTextSecondary = Color(0xFF94A3B8)

data class Intervention(
    val id: String,
    val title: String,
    val category: String,
    val location: String,
    val status: String,
    val dateTime: String,
    val techName: String
)

data class PassationLog(
    val id: String,
    val outTechName: String,
    val shiftTime: String,
    val remainingWork: String,
    val serviceKeysAndSalto: String,
    val securityKeysInfo: String,
    val outNotes: String,
    val timestamp: String,
    val isVerified: Boolean = false,
    val inTechName: String = "",
    val matchStatus: String = "",
    val inNotes: String = "",
    val verifyTime: String = ""
)

data class TechCatalog(
    val id: String,
    val title: String,
    val description: String,
    val uriString: String? = null
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HotelGmaoTechApp()
        }
    }
}

fun getCurrentDateTime(): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd | HH:mm", Locale.getDefault())
    return sdf.format(Date())
}

fun saveInterventions(context: Context, list: List<Intervention>) {
    val arr = JSONArray()
    list.forEach {
        val obj = JSONObject().apply {
            put("id", it.id)
            put("title", it.title)
            put("category", it.category)
            put("location", it.location)
            put("status", it.status)
            put("dateTime", it.dateTime)
            put("techName", it.techName)
        }
        arr.put(obj)
    }
    context.getSharedPreferences("GMAO_DB", Context.MODE_PRIVATE)
        .edit().putString("interventions", arr.toString()).apply()
}

fun loadInterventions(context: Context): List<Intervention> {
    val str = context.getSharedPreferences("GMAO_DB", Context.MODE_PRIVATE)
        .getString("interventions", null) ?: return emptyList()
    val arr = JSONArray(str)
    val list = mutableListOf<Intervention>()
    for (i in 0 until arr.length()) {
        val o = arr.getJSONObject(i)
        list.add(
            Intervention(
                o.getString("id"), o.getString("title"), o.getString("category"),
                o.getString("location"), o.getString("status"), o.getString("dateTime"),
                o.getString("techName")
            )
        )
    }
    return list
}

fun savePassations(context: Context, list: List<PassationLog>) {
    val arr = JSONArray()
    list.forEach {
        val obj = JSONObject().apply {
            put("id", it.id)
            put("outTechName", it.outTechName)
            put("shiftTime", it.shiftTime)
            put("remainingWork", it.remainingWork)
            put("serviceKeysAndSalto", it.serviceKeysAndSalto)
            put("securityKeysInfo", it.securityKeysInfo)
            put("outNotes", it.outNotes)
            put("timestamp", it.timestamp)
            put("isVerified", it.isVerified)
            put("inTechName", it.inTechName)
            put("matchStatus", it.matchStatus)
            put("inNotes", it.inNotes)
            put("verifyTime", it.verifyTime)
        }
        arr.put(obj)
    }
    context.getSharedPreferences("GMAO_DB", Context.MODE_PRIVATE)
        .edit().putString("passations", arr.toString()).apply()
}

fun loadPassations(context: Context): List<PassationLog> {
    val str = context.getSharedPreferences("GMAO_DB", Context.MODE_PRIVATE)
        .getString("passations", null) ?: return emptyList()
    val arr = JSONArray(str)
    val list = mutableListOf<PassationLog>()
    for (i in 0 until arr.length()) {
        val o = arr.getJSONObject(i)
        list.add(
            PassationLog(
                o.getString("id"), o.getString("outTechName"), o.getString("shiftTime"),
                o.getString("remainingWork"), o.getString("serviceKeysAndSalto"),
                o.getString("securityKeysInfo"), o.getString("outNotes"), o.getString("timestamp"),
                o.optBoolean("isVerified", false), o.optString("inTechName", ""),
                o.optString("matchStatus", ""), o.optString("inNotes", ""),
                o.optString("verifyTime", "")
            )
        )
    }
    return list
}

fun saveCatalogs(context: Context, list: List<TechCatalog>) {
    val arr = JSONArray()
    list.filter { it.uriString != null }.forEach {
        val obj = JSONObject().apply {
            put("id", it.id)
            put("title", it.title)
            put("description", it.description)
            put("uriString", it.uriString)
        }
        arr.put(obj)
    }
    context.getSharedPreferences("GMAO_DB", Context.MODE_PRIVATE)
        .edit().putString("catalogs", arr.toString()).apply()
}
fun loadCustomCatalogs(context: Context): List<TechCatalog> {
    val str = context.getSharedPreferences("GMAO_DB", Context.MODE_PRIVATE)
        .getString("catalogs", null) ?: return emptyList()
    val arr = JSONArray(str)
    val list = mutableListOf<TechCatalog>()
    for (i in 0 until arr.length()) {
        val o = arr.getJSONObject(i)
        list.add(
            TechCatalog(
                o.getString("id"), o.getString("title"),
                o.getString("description"), o.getString("uriString")
            )
        )
    }
    return list
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HotelGmaoTechApp() {
    val context = LocalContext.current
    var french by remember { mutableStateOf(false) }
    var currentTab by remember { mutableStateOf(0) }

    val interventions = remember { mutableStateListOf<Intervention>().apply { addAll(loadInterventions(context)) } }
    val passations = remember { mutableStateListOf<PassationLog>().apply { addAll(loadPassations(context)) } }

    val defaultCatalogs = listOf(
        TechCatalog("1", "أعطال التكييف المركزي و Split (Codes Erreur)", "رموز E1/E4، نقص الغاز، انسداد الفلتر، فحص الكارت الإلكتروني.", null),
        TechCatalog("2", "أقفال الغرف الإلكترونية (SALTO / VingCard)", "تغيير البطاريات، برمجة الكارت، فحص قارئ الباب (Reader) واللسان الميكانيكي.", null),
        TechCatalog("3", "اللوحات الكهربائية والإنارة (TGBT / Disjoncteurs)", "فحص القواطع التفاضلية، الكونتاكتورات، محولات 12V LED ومضخات الماء.", null)
    )
    val catalogs = remember {
        mutableStateListOf<TechCatalog>().apply {
            addAll(defaultCatalogs)
            addAll(loadCustomCatalogs(context))
        }
    }

    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = TechAccent,
            background = TechDarkBg,
            surface = TechCardBg,
            onBackground = TechTextPrimary,
            onSurface = TechTextPrimary
        )
    ) {
        Scaffold(
            containerColor = TechDarkBg,
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = TechCardBg),
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Build, contentDescription = null, tint = TechAccent, modifier = Modifier.size(26.dp))
                            Spacer(Modifier.width(10.dp))
                            Column {
                                Text("GMAO TECH PRO", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = TechTextPrimary)
                                Text(
                                    if (french) "Équipe Technique Hôtel" else "نظام الفريق التقني للصيانة",
                                    fontSize = 11.sp, color = TechBlue
                                )
                            }
                        }
                    },
                    actions = {
                        TextButton(onClick = { french = !french }) {
                            Text(if (french) "AR" else "FR", color = TechAccent, fontWeight = FontWeight.Bold)
                        }
                    }
                )
            },
            bottomBar = {
                NavigationBar(containerColor = TechCardBg) {
                    NavigationBarItem(
                        selected = currentTab == 0,
                        onClick = { currentTab = 0 },
                        icon = { Icon(Icons.Default.Home, null) },
                        label = { Text(if (french) "Accueil" else "الرئيسية", fontSize = 11.sp) }
                    )
                    NavigationBarItem(
                        selected = currentTab == 1,
                        onClick = { currentTab = 1 },
                        icon = { Icon(Icons.Default.Build, null) },
                        label = { Text(if (french) "Interventions" else "التدخلات", fontSize = 11.sp) }
                    )
                    NavigationBarItem(
                        selected = currentTab == 2,
                        onClick = { currentTab = 2 },
                        icon = { Icon(Icons.Default.Refresh, null) },
                        label = { Text(if (french) "Passation" else "البصاصيو", fontSize = 11.sp) }
                    )
                    NavigationBarItem(
                        selected = currentTab == 3,
                        onClick = { currentTab = 3 },
                        icon = { Icon(Icons.Default.Info, null) },
                        label = { Text(if (french) "Catalogues" else "الكتالوجات", fontSize = 11.sp) }
                    )
                }
            }
        ) { padding ->
            Box(Modifier.padding(padding)) {
                when (currentTab) {
                    0 -> DashboardScreen(french, interventions, passations, onNavigate = { currentTab = it })
                    1 -> InterventionsScreen(french, interventions) {
                        saveInterventions(context, interventions)
                    }
                    2 -> PassationScreen(french, passations) {
                        savePassations(context, passations)
                    }
                    3 -> CatalogsScreen(french, catalogs) {
                        saveCatalogs(context, catalogs)
                    }
                }
            }
        }
    }
}

@Composable
fun DashboardScreen(
    french: Boolean,
    interventions: List<Intervention>,
    passations: List<PassationLog>,
    onNavigate: (Int) -> Unit
) {
    val inProgress = interventions.count { it.status == "قيد التنفيذ" }
    val urgent = interventions.count { it.status == "عاجلة" }
    val done = interventions.count { it.status == "منجزة" }
    val unverifiedPassations = passations.count { !it.isVerified }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text(
                if (french) "Poste de Contrôle Technique" else "لوحة متابعة الفريق التقني",
                fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TechTextPrimary
            )
            Text(getCurrentDateTime(), color = TechBlue, fontSize = 13.sp)
        }

        item {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                TechKpiCard(if (french) "Urgentes" else "أعطال عاجلة", urgent.toString(), TechRed, Modifier.weight(1f))
                TechKpiCard(if (french) "En cours" else "قيد التنفيذ", inProgress.toString(), TechAccent, Modifier.weight(1f))
                TechKpiCard(if (french) "Terminées" else "منجزة", done.toString(), TechGreen, Modifier.weight(1f))
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth().clickable { onNavigate(2) },
                colors = CardDefaults.cardColors(containerColor = TechCardBg),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Lock, null, tint = TechAccent, modifier = Modifier.size(32.dp))
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        Text(
                            if (french) "Cahier de Passation & Clés" else "خزانة البصاصيو والمفاتيح",
                            fontWeight = FontWeight.Bold, color = TechTextPrimary
                        )
                        Text(
                            if (unverifiedPassations > 0) "يوجد ($unverifiedPassations) تسليم بانتظار تأكيد الاستلام!"
                            else "جميع تسليمات المناوبة مؤكدة ومستلمة.",
                            color = if (unverifiedPassations > 0) TechAccent else TechGreen,
                            fontSize = 12.sp
                        )
                    }
                    Icon(Icons.Default.ArrowForward, null, tint = TechTextSecondary)
                }
            }
        }

        item {
            Text(
                if (french) "Dernières interventions" else "آخر التدخلات التقنية",
                fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TechTextPrimary
            )
        }

        if (interventions.isEmpty()) {
            item {
                Text("لا توجد تدخلات مسجلة حالياً. اضغط على تبويب التدخلات لإضافة عطل.", color = TechTextSecondary, fontSize = 13.sp)
            }
        } else {
            items(interventions.take(5)) { item ->
                InterventionItemCard(item, onComplete = null)
            }
        }
    }
}

@Composable
fun TechKpiCard(title: String, count: String, color: Color, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = TechCardBg),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(count, fontSize = 26.sp, fontWeight = FontWeight.Bold, color = color)
            Spacer(Modifier.height(4.dp))
            Text(title, fontSize = 12.sp, color = TechTextSecondary)
        }
    }
}
@Composable
fun PassationScreen(
    french: Boolean,
    passations: MutableList<PassationLog>,
    onSave: () -> Unit
) {
    var showOutDialog by remember { mutableStateOf(false) }
    var verifyTarget by remember { mutableStateOf<PassationLog?>(null) }

    Scaffold(
        containerColor = TechDarkBg,
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showOutDialog = true },
                containerColor = TechAccent,
                contentColor = Color.Black,
                icon = { Icon(Icons.Default.Edit, null) },
                text = { Text("تسجيل نهاية المناوبة", fontWeight = FontWeight.Bold) }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    if (french) "Passation & Consignes Clés" else "خزانة البصاصيو وتسليم العهدة",
                    fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TechTextPrimary
                )
                Text("تتبع الخدمة المتبقية، مفاتيح السيرفيس، كارت Salto، ومفاتيح الأمن", color = TechTextSecondary, fontSize = 12.sp)
            }

            items(passations) { log ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = TechCardBg),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Row {
                            Text("المسلم (الخارج): ${log.outTechName}", fontWeight = FontWeight.Bold, color = TechAccent)
                            Spacer(Modifier.weight(1f))
                            Text(log.timestamp, fontSize = 11.sp, color = TechTextSecondary)
                        }
                        Text("التوقيت (دخول / خروج): ${log.shiftTime}", fontSize = 13.sp, color = TechBlue)
                        HorizontalDivider(color = TechDarkBg)
                        Text("• الأشغال المتبقية: ${log.remainingWork}", fontSize = 13.sp, color = TechTextPrimary)
                        Text("• عهدة المفاتيح وكارت Salto: ${log.serviceKeysAndSalto}", fontSize = 13.sp, color = TechTextPrimary)
                        Text("• مفاتيح مأخوذة من الأمن (Sécurité): ${log.securityKeysInfo}", fontSize = 13.sp, color = TechTextPrimary)
                        if (log.outNotes.isNotBlank()) {
                            Text("• ملاحظة الخارج: ${log.outNotes}", fontSize = 13.sp, color = TechTextSecondary)
                        }

                        Spacer(Modifier.height(6.dp))
                        if (!log.isVerified) {
                            Button(
                                onClick = { verifyTarget = log },
                                colors = ButtonDefaults.buttonColors(containerColor = TechBlue),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(Icons.Default.Check, null, tint = Color.Black)
                                Spacer(Modifier.width(6.dp))
                                Text("تأكيد الاستلام (للتقني الداخل)", color = Color.Black, fontWeight = FontWeight.Bold)
                            }
                        } else {
                            Surface(
                                color = if (log.matchStatus.contains("مطابق")) TechGreen.copy(alpha = 0.15f) else TechRed.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(Modifier.padding(10.dp)) {
                                    Text(
                                        "المستلم (الداخل): ${log.inTechName} (${log.verifyTime})",
                                        fontWeight = FontWeight.Bold,
                                        color = if (log.matchStatus.contains("مطابق")) TechGreen else TechRed,
                                        fontSize = 13.sp
                                    )
                                    Text("الحالة عند الاستلام: ${log.matchStatus}", fontSize = 13.sp, color = TechTextPrimary)
                                    if (log.inNotes.isNotBlank()) {
                                        Text("ملاحظة المستلم: ${log.inNotes}", fontSize = 12.sp, color = TechTextSecondary)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if (showOutDialog) {
            PassationOutDialog(
                onDismiss = { showOutDialog = false },
                onSubmit = { newLog ->
                    passations.add(0, newLog)
                    onSave()
                    showOutDialog = false
                }
            )
        }

        verifyTarget?.let { target ->
            PassationVerifyDialog(
                onDismiss = { verifyTarget = null },
                onConfirm = { inName, matchSt, inNote ->
                    val idx = passations.indexOf(target)
                    if (idx != -1) {
                        passations[idx] = target.copy(
                            isVerified = true,
                            inTechName = inName,
                            matchStatus = matchSt,
                            inNotes = inNote,
                            verifyTime = getCurrentDateTime()
                        )
                        onSave()
                    }
                    verifyTarget = null
                }
            )
        }
    }
}

@Composable
fun PassationOutDialog(onDismiss: () -> Unit, onSubmit: (PassationLog) -> Unit) {
    var outName by remember { mutableStateOf("") }
    var shiftTime by remember { mutableStateOf("") }
    var remainingWork by remember { mutableStateOf("") }
    var serviceKeys by remember { mutableStateOf("مفتاح السيرفيس + كارت جهاز Salto") }
    var securityKeys by remember { mutableStateOf("لا يوجد") }
    var notes by remember { mutableStateOf("") }

    AlertDialog(
        containerColor = TechCardBg,
        onDismissRequest = onDismiss,
        title = { Text("تقرير نهاية المناوبة (البصاصيو)", color = TechAccent, fontWeight = FontWeight.Bold) },
        text = {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.heightIn(max = 420.dp)) {
                item { OutlinedTextField(value = outName, onValueChange = { outName = it }, label = { Text("اسم التقني الخارج") }) }
                item { OutlinedTextField(value = shiftTime, onValueChange = { shiftTime = it }, label = { Text("وقت الدخول والخروج (مثال: 08:00 - 16:00)") }) }
                item { OutlinedTextField(value = remainingWork, onValueChange = { remainingWork = it }, label = { Text("واش خليت خدمة (الأشغال المتبقية)") }) }
                item { OutlinedTextField(value = serviceKeys, onValueChange = { serviceKeys = it }, label = { Text("مفاتيح السيرفيس وكارت Salto المسلمة") }) }
                item { OutlinedTextField(value = securityKeys, onValueChange = { securityKeys = it }, label = { Text("مفاتيح الأمن (تاع واش، علاش، وقتاش خرجت)") }) }
                item { OutlinedTextField(value = notes, onValueChange = { notes = it }, label = { Text("ملاحظات إضافية") }) }
            }
        },
        confirmButton = {
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = TechAccent),
                onClick = {
                    if (outName.isNotBlank()) {
                        onSubmit(
                            PassationLog(
                                id = UUID.randomUUID().toString(),
                                outTechName = outName,
                                shiftTime = shiftTime.ifBlank { "غير محدد" },
                                remainingWork = remainingWork.ifBlank { "لا يوجد" },
                                serviceKeysAndSalto = serviceKeys,
                                securityKeysInfo = securityKeys,
                                outNotes = notes,
                                timestamp = getCurrentDateTime()
                            )
                        )
                    }
                }
            ) {
                Text("تسجيل البصاصيو", color = Color.Black, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("إلغاء", color = TechTextSecondary) }
        }
    )
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PassationVerifyDialog(onDismiss: () -> Unit, onConfirm: (String, String, String) -> Unit) {
    var inName by remember { mutableStateOf("") }
    var matchStatus by remember { mutableStateOf("نعم، كل شيء مطابق لما كُتب") }
    var inNotes by remember { mutableStateOf("") }

    AlertDialog(
        containerColor = TechCardBg,
        onDismissRequest = onDismiss,
        title = { Text("تأكيد استلام المناوبة والعهدة", color = TechBlue, fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = inName, onValueChange = { inName = it }, label = { Text("اسم التقني المستلم (الداخل)") })
                Text("هل وجدت كل شيء مطابقاً لما كتبه زميلك؟", fontSize = 13.sp, color = TechTextPrimary)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(
                        selected = matchStatus.startsWith("نعم"),
                        onClick = { matchStatus = "نعم، كل شيء مطابق لما كُتب" },
                        label = { Text("نعم مطابق", fontSize = 12.sp) }
                    )
                    FilterChip(
                        selected = matchStatus.startsWith("لا"),
                        onClick = { matchStatus = "لا، يوجد نقص أو اختلاف" },
                        label = { Text("لا يوجد نقص", fontSize = 12.sp) }
                    )
                }
                OutlinedTextField(value = inNotes, onValueChange = { inNotes = it }, label = { Text("واش لقيت وملاحظاتك عند الاستلام") })
            }
        },
        confirmButton = {
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = TechGreen),
                onClick = {
                    if (inName.isNotBlank()) {
                        onConfirm(inName, matchStatus, inNotes)
                    }
                }
            ) {
                Text("تأكيد الاستلام", color = Color.Black, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("إلغاء", color = TechTextSecondary) }
        }
    )
}

@Composable
fun CatalogsScreen(
    french: Boolean,
    catalogs: MutableList<TechCatalog>,
    onSave: () -> Unit
) {
    val context = LocalContext.current
    var showAddDialog by remember { mutableStateOf(false) }
    var newTitle by remember { mutableStateOf("") }
    var newDesc by remember { mutableStateOf("") }
    var selectedUri by remember { mutableStateOf<Uri?>(null) }

    val pdfPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        uri?.let {
            try {
                context.contentResolver.takePersistableUriPermission(
                    it, Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            } catch (_: Exception) {}
            selectedUri = it
        }
    }

    Scaffold(
        containerColor = TechDarkBg,
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = TechAccent,
                contentColor = Color.Black,
                icon = { Icon(Icons.Default.Add, null) },
                text = { Text("إضافة كتالوج PDF", fontWeight = FontWeight.Bold) }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                Text(
                    if (french) "Catalogues & Pannes Fréquentes" else "كتالوجات الآلات والأعطال الشائعة",
                    fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TechTextPrimary
                )
            }

            items(catalogs) { cat ->
                Card(
                    modifier = Modifier.fillMaxWidth().clickable {
                        if (cat.uriString != null) {
                            try {
                                val intent = Intent(Intent.ACTION_VIEW).apply {
                                    setDataAndType(Uri.parse(cat.uriString), "application/pdf")
                                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                }
                                context.startActivity(intent)
                            } catch (e: Exception) {
                                Toast.makeText(context, "يرجى تثبيت قارئ PDF لفتح الملف", Toast.LENGTH_SHORT).show()
                            }
                        }
                    },
                    colors = CardDefaults.cardColors(containerColor = TechCardBg),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            if (cat.uriString != null) Icons.Default.List else Icons.Default.Build,
                            contentDescription = null,
                            tint = if (cat.uriString != null) TechRed else TechBlue,
                            modifier = Modifier.size(30.dp)
                        )
                        Spacer(Modifier.width(12.dp))
                        Column(Modifier.weight(1f)) {
                            Text(cat.title, fontWeight = FontWeight.Bold, color = TechTextPrimary, fontSize = 15.sp)
                            Spacer(Modifier.height(4.dp))
                            Text(cat.description, color = TechTextSecondary, fontSize = 12.sp)
                            if (cat.uriString != null) {
                                Text("اضغط لفتح ملف PDF", color = TechAccent, fontSize = 11.sp, modifier = Modifier.padding(top = 4.dp))
                            }
                        }
                    }
                }
            }
        }

        if (showAddDialog) {
            AlertDialog(
                containerColor = TechCardBg,
                onDismissRequest = { showAddDialog = false },
                title = { Text("إضافة كتالوج تقني (PDF)", color = TechTextPrimary, fontWeight = FontWeight.Bold) },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(value = newTitle, onValueChange = { newTitle = it }, label = { Text("اسم الآلة أو الكتالوج") })
                        OutlinedTextField(value = newDesc, onValueChange = { newDesc = it }, label = { Text("أبرز الأعطال أو الملاحظات") })
                        Button(
                            onClick = { pdfPickerLauncher.launch(arrayOf("application/pdf")) },
                            colors = ButtonDefaults.buttonColors(containerColor = TechBlue),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.Add, null, tint = Color.Black)
                            Spacer(Modifier.width(6.dp))
                            Text(if (selectedUri != null) "تم اختيار ملف PDF بنجاح" else "اختر ملف PDF من الهاتف", color = Color.Black)
                        }
                    }
                },
                confirmButton = {
                    Button(
                        colors = ButtonDefaults.buttonColors(containerColor = TechAccent),
                        onClick = {
                            if (newTitle.isNotBlank() && selectedUri != null) {
                                catalogs.add(
                                    TechCatalog(
                                        id = UUID.randomUUID().toString(),
                                        title = newTitle,
                                        description = newDesc.ifBlank { "كتالوج تقني مرفق بصيغة PDF" },
                                        uriString = selectedUri.toString()
                                    )
                                )
                                onSave()
                                newTitle = ""
                                newDesc = ""
                                selectedUri = null
                                showAddDialog = false
                            }
                        }
                    ) {
                        Text("حفظ الكتالوج", color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = onDismiss) { Text("إلغاء", color = TechTextSecondary) }
                }
            )
        }
    }
}
