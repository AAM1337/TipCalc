package com.bignerdranch.android.tipcalc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bignerdranch.android.tipcalc.ui.theme.TipCalcTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TipCalcTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    TipCalcScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun TipCalcScreen(modifier: Modifier = Modifier) {
    var billInput by remember { mutableStateOf("") }
    var dishesInput by remember { mutableStateOf("") }
    var tipsInput by remember { mutableStateOf("") }
    val bill = billInput.toFloatOrNull() ?: 0f
    var tipsAmount by remember { mutableStateOf(0f) }

    val dishes = dishesInput.toIntOrNull() ?: 0
    val discountPercent = when {
        dishes in 1..2 -> 3
        dishes in 3..5 -> 5
        dishes in 6..10 -> 7
        dishes > 10 -> 10
        else -> 0
    }

    var tipPercent by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(10f) }
    androidx.compose.foundation.layout.Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Сумма заказа:", modifier = Modifier.weight(1f))
            OutlinedTextField(
                value = billInput,
                onValueChange = { txt ->
                    billInput = txt
                    val b = txt.replace(',', '.').toFloatOrNull() ?: 0f
                    tipsAmount = b * tipPercent / 100f
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Количество блюд:", modifier = Modifier.weight(1f))
            OutlinedTextField(
                value = dishesInput,
                onValueChange = { dishesInput = it },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(Modifier.height(12.dp))
        Text("Чаевые:")
        Slider(
            value = tipPercent,
            onValueChange = {
                tipPercent = it.coerceIn(0f, 25f)
                tipsAmount = bill * tipPercent / 100f
            },
            valueRange = 0f..25f,
            modifier = Modifier.fillMaxWidth()
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("0")
            Text("25")
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Скидка:")
            listOf(3, 5, 7, 10).forEach { opt ->
                androidx.compose.foundation.layout.Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    RadioButton(
                        selected = discountPercent == opt,
                        onClick = { },
                        enabled = false
                    )
                    Text("$opt%")
                }
            }
        }
        Spacer(Modifier.height(24.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Сумма чаевых:", modifier = Modifier.weight(1f))
            OutlinedTextField(
                value = if (tipsAmount == 0f) "" else String.format("%.2f", tipsAmount),
                onValueChange = { txt ->
                    val newTips = txt.replace(',', '.').toFloatOrNull()
                    if (newTips != null) {
                        tipsAmount = newTips
                        if (tipPercent > 0f) {
                            val recalculatedBill = tipsAmount / (tipPercent / 100f)
                            billInput = String.format("%.2f", recalculatedBill)
                        }
                    } else {
                        tipsAmount = 0f
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f),
                singleLine = true,
                placeholder = { Text("0") }
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TipCalcPreview() {
    TipCalcTheme {
        TipCalcScreen(modifier = Modifier.padding(16.dp))
    }
}