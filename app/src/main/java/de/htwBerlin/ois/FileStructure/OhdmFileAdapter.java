package de.htwBerlin.ois.FileStructure;

import android.app.AlertDialog;
import android.content.Context;

import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import de.htwBerlin.ois.FTP.FtpTaskFileDownloading;
import de.htwBerlin.ois.R;

public class OhdmFileAdapter extends ArrayAdapter<OhdmFile> {

    private static final String TAG = "OhdmFileAdapter";

    private Context context;
    private int resource;

    public OhdmFileAdapter(Context context, int resource, ArrayList<OhdmFile> ohdmFiles){
        super(context, resource, ohdmFiles);
        this.context = context;
        this.resource = resource;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        final String fileName = getItem(position).getFilename();
        Long fileSize = getItem(position).getFileSize();
        String creationDate = getItem(position).getCreationDate();
        Boolean isDownloaded = getItem(position).isDownloaded();

        final OhdmFile ohdmFile = new OhdmFile(fileName, fileSize, creationDate, isDownloaded);

        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(resource, parent, false);

        TextView tvFileName = (TextView) convertView.findViewById(R.id.tvFileName);
        TextView tvFileSize = (TextView) convertView.findViewById(R.id.tvFileSize);
        TextView tvCreationDate = (TextView) convertView.findViewById(R.id.tvCreationDate);
        final Button buttonDownloadFile = (Button) convertView.findViewById(R.id.buttonDownloadFile);
        final ProgressBar progressBar = (ProgressBar) convertView.findViewById(R.id.progressBar);


        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Download finished");
        alertDialog.setMessage("You can now select the " + fileName + " from the main menu.");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        buttonDownloadFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FtpTaskFileDownloading ftpTaskFileDownloading = new FtpTaskFileDownloading(progressBar);
                Toast.makeText(getContext(),"Downloading " + fileName,Toast.LENGTH_SHORT).show();
                ftpTaskFileDownloading.execute(ohdmFile);
                disableButton(buttonDownloadFile);
                alertDialog.show();
            }
        });

        tvFileName.setText(fileName);
        tvFileSize.setText(fileSize + " kB");
        tvCreationDate.setText(creationDate);

        if (isDownloaded)   disableButton(buttonDownloadFile);

        return convertView;
    }

    private void disableButton(Button button){
        button.setEnabled(Boolean.FALSE);
    }
}



