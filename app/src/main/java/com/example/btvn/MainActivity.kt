package com.example.btvn

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.btvn.ui.theme.BTVNTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BTVNTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainAppNavigator(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun MainAppNavigator(modifier: Modifier = Modifier) {
    var currentScreen by remember { mutableStateOf("menu") }

    Box(modifier = modifier.fillMaxSize()) {
        when (currentScreen) {
            "menu" -> MenuScreen(
                onNavigateToCalculator = { currentScreen = "calculator" },
                onNavigateToStudents = { currentScreen = "student_list" },
                onNavigateToGrading = { currentScreen = "grading" },
                onNavigateToClassSummary = { currentScreen = "class_summary" }
            )
            "calculator" -> CalculatorScreen(onBack = { currentScreen = "menu" })
            "student_list" -> StudentListScreen(onBack = { currentScreen = "menu" })
            "grading" -> GradingScreen(onBack = { currentScreen = "menu" })
            "class_summary" -> ClassSummaryScreen(onBack = { currentScreen = "menu" })
        }
    }
}

@Composable
fun MenuScreen(
    onNavigateToCalculator: () -> Unit,
    onNavigateToStudents: () -> Unit,
    onNavigateToGrading: () -> Unit,
    onNavigateToClassSummary: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "BÀI TẬP UTH", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E3A8A))
        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = onNavigateToCalculator, modifier = Modifier.fillMaxWidth().height(50.dp)) {
            Text(text = "Bài 3: Ứng dụng Máy tính", fontSize = 16.sp)
        }
        Spacer(modifier = Modifier.height(12.dp))

        Button(onClick = onNavigateToStudents, modifier = Modifier.fillMaxWidth().height(50.dp)) {
            Text(text = "Bài 1: Danh sách sinh viên", fontSize = 16.sp)
        }
        Spacer(modifier = Modifier.height(12.dp))

        Button(onClick = onNavigateToGrading, modifier = Modifier.fillMaxWidth().height(50.dp)) {
            Text(text = "Bài 2: Xếp loại học lực (if/else)", fontSize = 16.sp)
        }
        Spacer(modifier = Modifier.height(12.dp))

        Button(onClick = onNavigateToClassSummary, modifier = Modifier.fillMaxWidth().height(50.dp)) {
            Text(text = "Bài 3 (BTVN): Tổng hợp bảng điểm lớp", fontSize = 16.sp)
        }
    }
}

// Hàm xếp loại học lực dùng chung
fun evaluateGrade(scoreValue: Double): Pair<String, Color> {
    return if (scoreValue < 0.0 || scoreValue > 10.0) {
        Pair("Không hợp lệ", Color.Gray)
    } else if (scoreValue >= 8.0) {
        Pair("Giỏi", Color(0xFF166534))
    } else if (scoreValue >= 6.5) {
        Pair("Khá", Color(0xFF1E40AF))
    } else if (scoreValue >= 5.0) {
        Pair("Trung bình", Color(0xFF9A3412))
    } else {
        Pair("Yếu", Color(0xFF991B1B))
    }
}

data class GradeStudent(val name: String, val score: Double?)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClassSummaryScreen(onBack: () -> Unit) {
    // Cập nhật điểm số theo yêu cầu: Ánh giỏi (9.0), Đức khá (7.2), Khoa TB (5.8), Khôi yếu (4.5), Vinh chưa có điểm (null)
    val studentList = listOf(
        GradeStudent("Hồ Thị Nguyệt Ánh", 9.0),
        GradeStudent("Đặng Song Đức", 7.2),
        GradeStudent("Huỳnh Đăng Khoa", 5.8),
        GradeStudent("Đinh Võ Như Khôi", 4.5),
        GradeStudent("Nguyễn Ngọc Quang Vinh", null)
    )

    var totalScore = 0.0
    var validScoreCount = 0
    var excellentCount = 0
    var missingScoreCount = 0
    var maxScore: Double? = null

    for (s in studentList) {
        if (s.score == null) {
            missingScoreCount++
        } else {
            val sc = s.score
            totalScore += sc
            validScoreCount++
            if (sc >= 8.0) {
                excellentCount++
            }
            if (maxScore == null || sc > maxScore) {
                maxScore = sc
            }
        }
    }

    val classAverage = if (validScoreCount > 0) totalScore / validScoreCount else 0.0

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Bảng điểm lớp", color = Color.White) },
                navigationIcon = {
                    TextButton(onClick = onBack) {
                        Text("← Về", color = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF1E3A8A))
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFF3F4F6))
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E3A8A))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(text = "Điểm trung bình lớp", fontSize = 13.sp, color = Color.White.copy(alpha = 0.8f))
                        Text(
                            text = String.format("%.2f", classAverage),
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(text = "Giỏi", fontSize = 12.sp, color = Color.White.copy(alpha = 0.8f))
                            Text(text = "$excellentCount", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                        Column {
                            Text(text = "Chưa có điểm", fontSize = 12.sp, color = Color.White.copy(alpha = 0.8f))
                            Text(text = "$missingScoreCount", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                        Column {
                            Text(text = "Cao nhất", fontSize = 12.sp, color = Color.White.copy(alpha = 0.8f))
                            Text(text = "${maxScore ?: 0.0}", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            }

            Text(
                text = "DANH SÁCH • ${studentList.size} SINH VIÊN",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Gray
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    for (student in studentList) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                Text(text = student.name, fontSize = 15.sp, fontWeight = FontWeight.Medium, color = Color(0xFF111827))
                                if (student.score == null) {
                                    Text(text = "Chưa có điểm", fontSize = 13.sp, color = Color.Gray)
                                } else {
                                    val (rankText, rankColor) = evaluateGrade(student.score)
                                    Text(text = rankText, fontSize = 13.sp, fontWeight = FontWeight.Medium, color = rankColor)
                                }
                            }

                            Text(
                                text = student.score?.let { if (it % 1.0 == 0.0) it.toInt().toString() else it.toString() } ?: "—",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF111827)
                            )
                        }
                        if (student != studentList.last()) {
                            HorizontalDivider(color = Color(0xFFF3F4F6))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CalculatorScreen(onBack: () -> Unit) {
    var num1 by remember { mutableStateOf("") }
    var num2 by remember { mutableStateOf("") }
    var result by remember { mutableStateOf<Double?>(null) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Thực hành 03: Máy tính", fontSize = 20.sp)
            TextButton(onClick = onBack) { Text("Quay lại") }
        }
        OutlinedTextField(value = num1, onValueChange = { num1 = it }, label = { Text("Nhập số thứ nhất") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = num2, onValueChange = { num2 = it }, label = { Text("Nhập số thứ hai") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.fillMaxWidth())
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { val n1 = num1.toDoubleOrNull(); val n2 = num2.toDoubleOrNull(); if (n1 != null && n2 != null) result = n1 + n2 }, modifier = Modifier.weight(1f)) { Text("+", fontSize = 18.sp) }
            Button(onClick = { val n1 = num1.toDoubleOrNull(); val n2 = num2.toDoubleOrNull(); if (n1 != null && n2 != null) result = n1 - n2 }, modifier = Modifier.weight(1f)) { Text("-", fontSize = 18.sp) }
            Button(onClick = { val n1 = num1.toDoubleOrNull(); val n2 = num2.toDoubleOrNull(); if (n1 != null && n2 != null) result = n1 * n2 }, modifier = Modifier.weight(1f)) { Text("*", fontSize = 18.sp) }
            Button(onClick = { val n1 = num1.toDoubleOrNull(); val n2 = num2.toDoubleOrNull(); if (n1 != null && n2 != null && n2 != 0.0) result = n1 / n2 }, modifier = Modifier.weight(1f)) { Text("/", fontSize = 18.sp) }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Kết quả: ${result?.let { if (it % 1.0 == 0.0) it.toInt().toString() else it.toString() } ?: ""}", fontSize = 18.sp)
    }
}

data class StudentItem(val name: String, val email: String?, val phone: String?)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentListScreen(onBack: () -> Unit) {
    val studentList = listOf(
        StudentItem("Đặng Song Đức", "ducds@ut.edu.vn", "0337111607"),
        StudentItem("Huỳnh Đăng Khoa", "khoahd6473@ut.edu.vn", "0342795707"),
        StudentItem("Nguyễn Ngọc Quang Vinh", null, "039231129"),
        StudentItem("Đinh Võ Như Khôi", "khoicm5986@ut.edu.vn", null),
        StudentItem("Hồ Thị Nguyệt Ánh", null, null)
    )
    var missingEmailCount = 0
    for (s in studentList) { if (s.email == null) missingEmailCount++ }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Danh sách sinh viên", color = Color.White) },
                navigationIcon = { TextButton(onClick = onBack) { Text("← Về", color = Color.White) } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF1E3A8A))
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding).background(Color(0xFFF3F4F6)).verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            for (student in studentList) {
                Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(text = student.name, fontSize = 16.sp, color = Color(0xFF111827))
                        Text(text = "Email: ${student.email ?: "Chưa cập nhật"}", fontSize = 14.sp, color = Color(0xFF4B5563))
                        Text(text = "SĐT: ${student.phone ?: "Chưa cập nhật"}", fontSize = 14.sp, color = Color(0xFF4B5563))
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth().background(Color.White).padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = "Tổng: ${studentList.size} sinh viên", fontSize = 14.sp, color = Color(0xFF1E3A8A))
                Text(text = "Thiếu email: $missingEmailCount", fontSize = 14.sp, color = Color(0xFF1E3A8A))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GradingScreen(onBack: () -> Unit) {
    val currentScore = 8.5
    val (rankText, rankColor) = evaluateGrade(currentScore)
    val testScores = listOf(9.0, 7.0, 5.5, 3.0, 11.0)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Xếp loại học lực", color = Color.White) },
                navigationIcon = { TextButton(onClick = onBack) { Text("← Về", color = Color.White) } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF1E3A8A))
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding).background(Color(0xFFF3F4F6)).verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Column(modifier = Modifier.fillMaxWidth().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(text = "Điểm trung bình", fontSize = 14.sp, color = Color.Gray)
                    Text(text = currentScore.toString(), fontSize = 40.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    Box(modifier = Modifier.fillMaxWidth().background(rankColor.copy(alpha = 0.15f), shape = MaterialTheme.shapes.medium).padding(vertical = 12.dp), contentAlignment = Alignment.Center) {
                        Text(text = rankText, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = rankColor)
                    }
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "KIỂM TRA THÊM", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    for (score in testScores) {
                        val (rText, rColor) = evaluateGrade(score)
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "Điểm ${if (score % 1.0 == 0.0) score.toInt().toString() else score.toString()}", fontSize = 15.sp, color = Color(0xFF374151))
                            Box(modifier = Modifier.background(rColor.copy(alpha = 0.1f), shape = MaterialTheme.shapes.small).padding(horizontal = 12.dp, vertical = 6.dp)) {
                                Text(text = rText, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = rColor)
                            }
                        }
                        if (score != testScores.last()) { HorizontalDivider(color = Color(0xFFF3F4F6)) }
                    }
                }
            }
        }
    }
}