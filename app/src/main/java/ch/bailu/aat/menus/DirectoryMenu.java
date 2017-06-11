package ch.bailu.aat.menus;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;

import ch.bailu.aat.R;
import ch.bailu.aat.preferences.SolidFile;
import ch.bailu.aat.util.Clipboard;
import ch.bailu.aat.util.fs.FileIntent;

public class DirectoryMenu extends AbsMenu {

    private MenuItem browse, view, get, clipboard, permission;

    private final Activity acontext;

    private final SolidFile sdirectory;
    public DirectoryMenu(Activity c, SolidFile d) {
        acontext = c;
        sdirectory = d;
    }


    @Override
    public void inflate(Menu menu) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            browse = menu.add("Pick directory...*");
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            permission = menu.add("Permission");
        }

        view = menu.add(R.string.file_view);
        get = menu.add(R.string.file_view);
        clipboard = menu.add(R.string.clipboard_copy);

    }

    @Override
    public void inflateWithHeader(ContextMenu menu) {
        menu.setHeaderTitle(sdirectory.getValueAsFile().getName());
        menu.setHeaderIcon(R.drawable.folder_inverse);
        inflate(menu);
    }

    @Override
    public void prepare(Menu menu) {

    }

    @Override
    public boolean onItemClick(MenuItem item) {
        Context c = sdirectory.getContext();

        if (item == browse) {
            sdirectory.setFromPickerActivity(acontext);

        } else if  (item == view) {
            FileIntent.view(c, new Intent(), sdirectory.getValueAsFile());

        } else if (item == get) {
            FileIntent.browse(c, new Intent(),
                    Uri.parse(sdirectory.getValueAsFile().toString()));

        } else if (item == clipboard) {
            new Clipboard(c).setText(sdirectory.getLabel(),
                    sdirectory.getValueAsString());
        }
            /*
        } else if (item == permission) {
            new AndroidVolumes(acontext).askForPermission(acontext, sdirectory.getValueAsFile());
        }
*/
        return true;
    }


}


