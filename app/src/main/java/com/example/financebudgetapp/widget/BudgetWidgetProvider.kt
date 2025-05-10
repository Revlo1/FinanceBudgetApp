package com.example.financebudgetapp.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.SharedPreferences
import android.widget.RemoteViews
import com.example.financebudgetapp.R
import java.text.DecimalFormat

class BudgetWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    companion object {
        internal fun updateAppWidget(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetId: Int
        ) {
            // Read both budget and total_spent from SharedPreferences
            val sharedPreferences = context.getSharedPreferences("FinancePrefs", Context.MODE_PRIVATE)
            val budget = sharedPreferences.getFloat("budget", 0.0f).toDouble()
            val totalSpent = sharedPreferences.getFloat("total_spent", 0.0f).toDouble()

            // Format the values
            val decimalFormat = DecimalFormat("0.00")
            val formattedTotal = decimalFormat.format(totalSpent)
            val formattedBudget = decimalFormat.format(budget)

            // Create a RemoteViews object using the updated layout
            val views = RemoteViews(context.packageName, R.layout.widget_layout)

            // Update the TextView with the budget overview
            views.setTextViewText(
                R.id.budget_overview_text, // Use the budget_overview_text ID
                "Spent: £$formattedTotal / Budget: £$formattedBudget"
            )

            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}