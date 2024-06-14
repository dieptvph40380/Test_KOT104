package com.example.test_kot104.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.test_kot104.room.SinhVienDB

import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.test_kot104.viewmodel.SinhVienModel


@Composable
fun SinhVienScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val db = Room.databaseBuilder(
        context,
        SinhVienDB::class.java, "sinhvien-db"
    ).allowMainThreadQueries().build()

    var listSinhViens by remember { mutableStateOf(db.sinhvienDAO().getAll()) }
    var editingSinh by remember { mutableStateOf<SinhVienModel?>(null) }
    var showingAddSinhDialog by remember { mutableStateOf(false) }
    var showingSinhDetail by remember { mutableStateOf<SinhVienModel?>(null) }

    /// Gia dien
    Column(Modifier.fillMaxWidth()) {
        Text(
            text = "Quản lý sinh viên",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(16.dp)
        )
        Button(
            onClick = { showingAddSinhDialog =true},
            modifier = Modifier.padding(16.dp)
            ) {
            Text(text = "Thêm SV")
        }

        //// Cac truomg du lieu hien thi o day
        LazyVerticalGrid(columns = GridCells.Fixed(3),
            Modifier.fillMaxSize(),
            contentPadding = PaddingValues(8.dp)
            ) {
            items(listSinhViens){
                sv ->
                Column(
                    Modifier
                    .fillMaxWidth()
                    .padding(16.dp).background(color = Color.Gray)
                    .clickable { showingSinhDetail = sv }
                ) {
                    sv.anh?.let{anh ->
                        if(anh.isNotEmpty()){
                            Image(
                                painter = rememberAsyncImagePainter(anh),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(100.dp) // Adjust size as needed
                                    .padding(end = 8.dp).clip(RoundedCornerShape(20.dp)) // Bo tròn góc của hình ảnh// Add some padding
                            )
                        }
                    }
                    Text(
                        text = "ID: ${sv.uid}",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    Text(
                        text = "Tên: ${sv.hoten}",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    Button(onClick = { editingSinh = sv }) {
                        Text(text = "Sửa")
                    }
                    Button(onClick = {
                        db.sinhvienDAO().delete(sv)
                        listSinhViens = db.sinhvienDAO().getAll()
                    }) {
                        Text(text = "Xóa")
                    }
                }
                Divider()
            }
        }
        if(showingAddSinhDialog){
            AddTranhDialog(
                onDismiss = { showingAddSinhDialog = false },
                onSave = { newSv ->
                    db.sinhvienDAO().insert(newSv)
                    listSinhViens = db.sinhvienDAO().getAll()
                    showingAddSinhDialog = false
                }
            )
        }
        editingSinh?.let { sv ->
            EditTranhDialog(
                sv = sv,
                onDismiss = { editingSinh = null },
                onSave = { updatedTranh ->
                    db.sinhvienDAO().update(updatedTranh)
                    listSinhViens = db.sinhvienDAO().getAll()
                    editingSinh = null
                }
            )
        }
        showingSinhDetail?.let { tranh ->
            TranhDetailScreen(
                sv = tranh,
                onDismiss = { showingSinhDetail = null }
            )
        }

    }
}

@Composable
fun EditTranhDialog(sv: SinhVienModel, onDismiss: () -> Unit, onSave: (SinhVienModel)->Unit) {
    var hoten by remember { mutableStateOf(sv.hoten ?: "") }
    var diem by remember { mutableStateOf(sv.diem?.toString() ?: "0") }
    var statusSv by remember { mutableStateOf(sv.statusSv ?: false) }
    var anh by remember { mutableStateOf(sv.anh ?: "") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Sửa Sinh viên") },
        text = {
            Column {
                TextField(
                    value = hoten,
                    onValueChange = { hoten = it },
                    label = { Text("Họ tên") }
                )

                TextField(
                    value = diem,
                    onValueChange = { diem = it },
                    label = { Text("Điểm TB") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = statusSv,
                        onCheckedChange = { statusSv = it }
                    )
                    Text(text = "Đã ra trường")
                }
                TextField(
                    value = anh,
                    onValueChange = { anh = it },
                    label = { Text("Đường dẫn ảnh") }
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                val updatedStudent = sv.copy(
                    hoten = hoten,
                    diem = diem.toFloatOrNull() ?: 0f,
                    statusSv = statusSv,
                    anh = anh
                )
                onSave(updatedStudent)
            }) {
                Text("Lưu")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Hủy")
            }
        }
    )
}
@Composable
fun AddTranhDialog(onDismiss: () -> Unit, onSave: (SinhVienModel) -> Unit) {
    var hoten by remember { mutableStateOf("") }
    var diem by remember { mutableStateOf("") }
    var statusSv by remember { mutableStateOf(false) }
    var anh by remember { mutableStateOf("") }
    var nameError by remember { mutableStateOf("") }
    var priceError by remember { mutableStateOf("") }
    var photoPathError by remember { mutableStateOf("") }

    // Validation function
    fun validateInputs(): Boolean {
        var isValid = true

        if (hoten.isBlank()) {
            nameError = "Tên tranh không được để trống"
            isValid = false
        } else {
            nameError = ""
        }

        if (diem.isBlank()) {
            priceError = "Giá không được để trống"
            isValid = false
        } else if (diem.toFloatOrNull() == null) {
            priceError = "Giá phải là một số"
            isValid = false
        } else {
            priceError = ""
        }

        if (anh.isBlank()) {
            photoPathError = "Đường dẫn ảnh không được để trống"
            isValid = false
        } else {
            photoPathError = ""
        }

        return isValid
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Thêm Tranh") },
        text = {
            Column {
                TextField(
                    value = hoten,
                    onValueChange = { hoten = it },
                    label = { Text("Tên tranh") },
                    isError = nameError.isNotEmpty()
                )
                if (nameError.isNotEmpty()) {
                    Text(
                        text = nameError,
                        color = Color.Red,
                        style = MaterialTheme.typography.titleLarge
                    )
                }

                TextField(
                    value = diem,
                    onValueChange = { diem = it },
                    label = { Text("Điểm") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    isError = priceError.isNotEmpty()
                )
                if (priceError.isNotEmpty()) {
                    Text(
                        text = priceError,
                        color = Color.Red,
                        style = MaterialTheme.typography.titleLarge
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = statusSv,
                        onCheckedChange = { statusSv = it }
                    )
                    Text(text = "Trạng thái")
                }

                TextField(
                    value = anh,
                    onValueChange = { anh = it },
                    label = { Text("Đường dẫn ảnh") },
                    isError = photoPathError.isNotEmpty()
                )
                if (photoPathError.isNotEmpty()) {
                    Text(
                        text = photoPathError,
                        color = Color.Red,
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                if (validateInputs()) {
                    val newSv = SinhVienModel(
                        hoten = hoten,
                        diem = diem.toFloatOrNull() ?: 0f,
                        statusSv = statusSv,
                        anh = anh
                    )
                    onSave(newSv)
                }
            }) {
                Text("Lưu")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Hủy")
            }
        }
    )
}

@Composable
fun TranhDetailScreen(sv: SinhVienModel, onDismiss: () -> Unit) {
// Trien khai alert
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "chi tiet tranh") },
        text = {
            Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Họ tên: ${sv.hoten}")
                Text(text = "Điểm TB: ${sv.diem}")
                Text(text = "Đã ra trường: ${if (sv.statusSv == true) "Có" else "Không"}")
                sv.anh?.let {
                    if (it.isNotEmpty()) {
                        Image(
                            painter = rememberAsyncImagePainter(it),
                            contentDescription = null,
                            modifier = Modifier.size(100.dp)
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("Đóng")
            }
        }
    )
}