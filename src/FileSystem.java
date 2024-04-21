public class FileSystem {
    private Directory root;
    private Directory currentDirectory;

    public FileSystem() {
        // Sistemi kök dizinle başlatır
        this.root = new Directory("rd", null);
        this.currentDirectory = this.root; // Başlangıçta mevcut dizin kök dizindir.
    }

    public Directory getRoot() {
        return root;
    }

    // Yeni bir dosya oluşturma metodu
    public void createFile(String name, Directory parent) {
        if (parent != null) {
            File newFile = new File(name, parent);
            parent.addElement(newFile);
        }
    }

    // Yeni bir dizin oluşturma metodu
    public void createDirectory(String name, Directory parent) {
        if (parent != null) {
            Directory newDirectory = new Directory(name, parent);
            parent.addElement(newDirectory);
        }
    }

    // Belirli bir dosyayı silme metodu
    public void deleteFile(String name, Directory parent) {
        if (parent != null) {
            FileSystemElement toDelete = findElementByName(name, parent);
            if (toDelete instanceof File) {
                parent.removeElement(toDelete);
            }
        }
    }

    // Belirli bir dizini ve içeriğini silme metodu
    public void deleteDirectory(String name, Directory parent) {
        if (parent != null) {
            FileSystemElement toDelete = findElementByName(name, parent);
            if (toDelete instanceof Directory) {
                parent.removeElement(toDelete);
            }
        }
    }

    // Adına göre bir eleman bulma yardımcı metodu
    public FileSystemElement findElementByName(String name, Directory parent) {
        for (FileSystemElement elem : parent.getChildren()) {
            if (elem.getName().equals(name)) {
                return elem;
            }
        }
        return null;
    }

    // Mevcut yolu döndüren metod
    public String getCurrentPath() {
        if (currentDirectory == null) {
            return "/";
        }

        FileSystemElement elem = currentDirectory;
        StringBuilder path = new StringBuilder(elem.getName());

        while (elem.getParent() != null) {
            elem = elem.getParent();
            path.insert(0, elem.getName() + "/");
        }

        return path.toString();
    }
    // Yeni eklenecek 'changeDirectory' metodu
    public void changeDirectory(String path) {
        if (path.equals("/")) {
            // Eğer yol sadece '/' ise kök dizine git
            currentDirectory = root;
            return;
        }

        // Kök dizinden başlayarak yolu bölümlere ayır
        String[] pathComponents = path.split("/");
        Directory targetDirectory = root;

        for (String component : pathComponents) {
            if (component.isEmpty() || component.equals(".")) {
                // Yolun bu bölümü geçerli dizini işaret ediyor, atla
                continue;
            } else if (component.equals("..")) {
                // Yolun bu bölümü bir üst dizini işaret ediyor
                if (targetDirectory.getParent() != null) {
                    // Üst dizin varsa, ona git
                    targetDirectory = (Directory) targetDirectory.getParent();
                }
            } else {
                // Yolun bu bölümü bir alt dizini işaret ediyor
                boolean found = false;
                for (FileSystemElement elem : targetDirectory.getChildren()) {
                    if (elem instanceof Directory && elem.getName().equals(component)) {
                        // Hedef alt dizini bulduk, bu yeni hedef dizinimiz olacak
                        targetDirectory = (Directory) elem;
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    // Eğer belirtilen adla bir alt dizin bulunamadıysa,
                    // yolu takip etmeyi bırak ve hata mesajı yazdır
                    System.out.println("Directory not found: " + component);
                    return;
                }
            }
        }

        // Yol başarıyla bulundu, mevcut dizini güncelle
        currentDirectory = targetDirectory;
    }
    // Mevcut çalışma dizinini döndüren metod
    public Directory getCurrentDirectory() {
        return currentDirectory;
    }

    // Mevcut dizinin içeriğini listeleme metodu
    public void listDirectoryContents() {
        System.out.println("Contents of Directory: " + currentDirectory.getName());
        for (FileSystemElement elem : currentDirectory.getChildren()) {
            if (elem instanceof Directory) {
                System.out.println("Directory: " + elem.getName());
            } else if (elem instanceof File) {
                System.out.println("File: " + elem.getName());
            }
        }
    }
    public Directory findDirectoryByPath(String path) {
        if (path.equals("/")) {
            return root; // Kök dizini döndür
        }

        String[] pathComponents = path.split("/");
        Directory target = root;

        for (String component : pathComponents) {
            if (component.isEmpty() || component.equals(".")) {
                continue; // Geçerli dizini gösterir, döngüde bir sonraki iterasyona geç
            } else if (component.equals("..")) {
                // Bir üst dizine çık
                if (target.getParent() != null) {
                    target = (Directory)target.getParent();
                }
            } else {
                // Bir alt dizine in
                Directory nextDir = target.getChildDirectory(component);
                if (nextDir == null) {
                    return null; // Hedef yol geçersizse null döndür
                }
                target = nextDir;
            }
        }

        return target; // Hedef dizini döndür
    }

    // Yardımcı metod, bir Directory'nin altındaki belirli bir isimdeki child Directory'yi bulur
    private Directory getChildDirectory(String name) {
        for (FileSystemElement elem : currentDirectory.getChildren()) {
            if (elem instanceof Directory && elem.getName().equals(name)) {
                return (Directory)elem;
            }
        }
        return null; // Eğer belirtilen isimde bir child Directory yoksa null döndür
    }
}
