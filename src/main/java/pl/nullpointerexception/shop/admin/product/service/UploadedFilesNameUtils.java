package pl.nullpointerexception.shop.admin.product.service;

import com.github.slugify.Slugify;
import org.apache.commons.io.FilenameUtils;

class UploadedFilesNameUtils {
    public static String slugifyFileName(String filename) {
        String name = FilenameUtils.getBaseName(filename);

        Slugify slg = new Slugify();
        String changedName = slg
                .withCustomReplacement("_","-")
                .slugify(name);

        return changedName + "." + FilenameUtils.getExtension(filename);
    }
}
