package com.example.gymbox_semaforo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<String[]> dataRows = readCSV();

        // Obter a hora atual
        Calendar now = Calendar.getInstance();
        int currentDay = now.get(Calendar.DAY_OF_WEEK);
        int currentHour = now.get(Calendar.HOUR_OF_DAY);
        int currentMinute = now.get(Calendar.MINUTE);

        // Obtenha as referências dos elementos da interface do usuário
        TextView textViewGymStatus = findViewById(R.id.textViewGymStatus);
        ImageView imageViewSemaphore = findViewById(R.id.imageViewSemaphore);

        if ((currentDay >= Calendar.MONDAY && currentDay <= Calendar.FRIDAY && currentHour >= 8 && (currentHour < 22 || (currentHour == 22 && currentMinute == 0)))
                || (currentDay == Calendar.SATURDAY && currentHour >= 9 && (currentHour < 18 || (currentHour == 18 && currentMinute == 0)))) {

            int entriesCount = 0;
            for (String[] row : dataRows) {
                int entryHour = Integer.parseInt(row[7]);    // Supondo que o campo 'hour' é o 8º campo

                // Comparar com a hora atual
                if (entryHour == currentHour) {
                    entriesCount++;
                }
            }

            // Definir a cor do semáforo
            String semaphoreColor;
            if (entriesCount <= 2000) {
                semaphoreColor = "GREEN";
            } else if (entriesCount <= 2200) {
                semaphoreColor = "YELLOW";
            } else {
                semaphoreColor = "RED";
            }

            // Definir a cor do semáforo no ImageView
            int colorResId;
            switch (semaphoreColor) {
                case "YELLOW":
                    colorResId = R.color.yellow;
                    break;
                case "RED":
                    colorResId = R.color.red;
                    break;
                default:
                    colorResId = R.color.green;
                    break;
            }
            imageViewSemaphore.setColorFilter(ContextCompat.getColor(this, colorResId));

            // Mostrar a mensagem de horário do ginásio
            String gymStatusMessage = "Ginásio aberto";
            textViewGymStatus.setText(gymStatusMessage);

            // Agora você pode utilizar a variável 'semaphoreColor' para mostrar o semáforo na sua UI
        } else {
            // Ginásio não está aberto, mostrar mensagem e horário
            String gymClosedMessage = "Ginásio fechado";
            String gymOpeningHours = "Horário do ginásio:\nSeg-Sex: 8:30h-22:00h\nSáb: 9:00h-18:00h";
            textViewGymStatus.setText(gymClosedMessage + "\n\n" + gymOpeningHours);
        }
    }

    public List<String[]> readCSV() {
        List<String[]> rows = new ArrayList<>();
        AssetManager assetManager = getAssets();

        try {
            InputStream csvStream = assetManager.open("yourfile.csv");
            InputStreamReader csvStreamReader = new InputStreamReader(csvStream);
            CSVReader csvReader = new CSVReader(csvStreamReader);
            String[] nextLine;

            while ((nextLine = csvReader.readNext()) != null) {
                rows.add(nextLine);
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
        return rows;
    }

}