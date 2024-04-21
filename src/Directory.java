import java.util.LinkedList;
import java.util.List;

public class Directory extends FileSystemElement {
    private List<FileSystemElement> children;

    public Directory(String name, FileSystemElement parent) {
        super(name, parent);
        this.children = new LinkedList<>();
    }

    public void addElement(FileSystemElement element) {
        if (element != null) {
            children.add(element);
            element.parent = this; // Çocuğun üst referansını güncelle
        }
    }

    public void removeElement(FileSystemElement element) {
        if (element != null) {
            children.remove(element);
            element.parent = null; // Çocuğun üst referansını kaldır
        }
    }

    @Override
    public void print(String prefix) {
        System.out.println(prefix + "Directory: " + getName());
        for (FileSystemElement elem : children) {
            elem.print(prefix + "    "); // Rekürsif olarak çocukları yazdır
        }
    }

    // Bu sınıfa özgü diğer metodlar veya özellikler buraya eklenebilir.
    // Örneğin, içerdiği elemanları döndüren bir listeleme metodu.

    public List<FileSystemElement> getChildren() {
        return new LinkedList<>(children); // Çocukların bir kopyasını döndürür
    }

    public void moveElement(FileSystemElement elementToMove, Directory newParent) {
        if (elementToMove != null && newParent != null && !newParent.equals(elementToMove.parent)) {
            // Mevcut ebeveynin çocuk listesinden öğeyi kaldır
            if (elementToMove.parent != null) {
                ((Directory) elementToMove.parent).removeElement(elementToMove);
            }

            // Yeni ebeveynin çocuk listesine öğeyi ekle
            newParent.addElement(elementToMove);
        }
    }

    // Bu metod, dizini ve içindeki tüm içeriği yinelemeli olarak siler.
    public void delete() {
        // Geçici bir liste oluşturmak, ConcurrentModificationException önler
        List<FileSystemElement> tempChildren = new LinkedList<>(children);
        for (FileSystemElement elem : tempChildren) {
            if (elem instanceof Directory) {
                // Alt dizini ve içeriğini yinelemeli olarak sil
                ((Directory) elem).delete();
            }
            // Öğeyi çocuk listesinden kaldır
            removeElement(elem);
        }
        // Eğer bu dizinin bir üst dizini varsa, bu dizini de kaldır
        if (this.parent != null) {
            ((Directory) this.parent).removeElement(this);
        }
    }

    public FileSystemElement search(String name) {
        // Önce bu dizinde arama yap
        for (FileSystemElement elem : children) {
            if (elem.getName().equals(name)) {
                // Aranan öğe bu dizinde bulundu
                return elem;
            }
        }

        // Sonra alt dizinlerde yinelemeli olarak arama yap
        for (FileSystemElement elem : children) {
            if (elem instanceof Directory) {
                FileSystemElement found = ((Directory) elem).search(name);
                if (found != null) {
                    // Aranan öğe bir alt dizinde bulundu
                    return found;
                }
            }
        }

        // Aranan öğe bu dizinde veya alt dizinlerinde bulunamadı
        return null;
    }
    // Mevcut dizinin içeriğini listeleme metodu
    public void listContents() {
        System.out.println("Contents of Directory: " + getName());
        for (FileSystemElement elem : children) {
            // Öğe bir dosya ise basitçe adını yazdır
            if (elem instanceof File) {
                System.out.println("File: " + elem.getName());
            }
            // Öğe bir dizin ise bir işaret ekleyerek adını yazdır
            else if (elem instanceof Directory) {
                System.out.println("Directory: " + elem.getName());
            }
        }
    }
    // Belirtilen adla bir alt dizini bulan ve döndüren bir yardımcı metod
    public Directory getChildDirectory(String name) {
        for (FileSystemElement elem : children) {
            if (elem instanceof Directory && elem.getName().equals(name)) {
                return (Directory) elem;
            }
        }
        return null; // Eğer belirtilen isimde bir alt dizin yoksa null döndür
    }

}
