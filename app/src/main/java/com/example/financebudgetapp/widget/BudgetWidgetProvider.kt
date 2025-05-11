package com.example.financebudgetapp.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
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
            //read from SharedPreferences
            val sharedPreferences = context.getSharedPreferences("FinancePrefs", Context.MODE_PRIVATE)
            val budget = sharedPreferences.getFloat("budget", 0.0f).toDouble()
            val totalSpent = sharedPreferences.getFloat("total_spent", 0.0f).toDouble()

            //format values
            val decimalFormat = DecimalFormat("0.00")
            val formattedTotal = decimalFormat.format(totalSpent)
            val formattedBudget = decimalFormat.format(budget)

            //RemoteViews used for widget layout
            val views = RemoteViews(context.packageName, R.layout.widget_layout)


            views.setTextViewText(
                R.id.budget_overview_text, // Use the budget_overview_text ID
                "Spent: £$formattedTotal / Budget: £$formattedBudget"
            )

            //update widget
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}