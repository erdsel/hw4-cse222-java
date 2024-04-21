import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static FileSystem fileSystem = new FileSystem();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            // Menüyü göster
            System.out.println("===== File System Management Menu =====");
            System.out.println("1. Change directory");
            System.out.println("2. List directory contents");
            System.out.println("3. Create file/directory");
            System.out.println("4. Delete file/directory");
            System.out.println("5. Move file/directory");
            System.out.println("6. Search file/directory");
            System.out.println("7. Print directory tree");
            System.out.println("8. Sort contents by date created");
            System.out.println("9. Exit");
            System.out.print("Please select an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Girdi akışını temizlemek için

            switch (choice) {
                case 1:
                    // Change directory
                    changeDirectory();
                    break;
                case 2:
                    // List directory contents
                    listDirectoryContents();
                    break;
                case 3:
                    // Create file/directory
                    createFileOrDirectory();
                    break;
                case 4:
                    // Delete file/directory
                    deleteFileOrDirectory();
                    break;
                case 5:
                    // Move file/directory
                    moveFileOrDirectory();
                    break;
                case 6:
                    // Search file/directory
                    searchFileOrDirectory();
                    break;
                case 7:
                    // Print directory tree
                    printDirectoryTree();
                    break;
                case 8:
                    // Sort contents by date created
                    sortContentsByDate();
                    break;
                case 9:
                    // Exit
                    System.out.println("Exiting the file system.");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        }
    }

    // Aşağıdaki metodlar gereken işlemleri gerçekleştirmek için 
    // gerekli mantığı içerecek şekilde tanımlanmalıdır.
    private static void changeDirectory() {
        System.out.print("Enter new directory path: ");
        String path = scanner.nextLine();
        fileSystem.changeDirectory(path);
    }

    private static void listDirectoryContents() {
        fileSystem.listDirectoryContents();
    }

    private static void createFileOrDirectory() {
        System.out.println("Create file or directory (f/d): ");
        String type = scanner.nextLine().trim();
        String name;

        switch (type) {
            case "f":
                // Dosya oluşturma
                System.out.println("Enter name for new file: ");
                name = scanner.nextLine().trim();
                fileSystem.createFile(name, fileSystem.getCurrentDirectory());
                System.out.println("File created: " + name);
                break;
            case "d":
                // Dizin oluşturma
                System.out.println("Enter name for new directory: ");
                name = scanner.nextLine().trim();
                fileSystem.createDirectory(name, fileSystem.getCurrentDirectory());
                System.out.println("Directory created: " + name + "/");
                break;
            default:
                System.out.println("Invalid type. Please enter 'f' for file or 'd' for directory.");
                break;
        }
    }

    private static void deleteFileOrDirectory() {
        System.out.println("Current directory: " + fileSystem.getCurrentPath());
        System.out.print("Enter name of file/directory to delete: ");
        String name = scanner.nextLine().trim();

        // Bulunan öğeyi silmek için fileSystem nesnesini kullan
        FileSystemElement elementToDelete = fileSystem.findElementByName(name, fileSystem.getCurrentDirectory());

        // Eğer öğe bulunamadıysa, kullanıcıyı bilgilendir
        if (elementToDelete == null) {
            System.out.println("File/directory not found: " + name);
            return;
        }

        // Eğer öğe bir dosya ise, dosyayı sil
        if (elementToDelete instanceof File) {
            fileSystem.deleteFile(name, fileSystem.getCurrentDirectory());
            System.out.println("File deleted: " + name);
        }
        // Eğer öğe bir dizin ise, dizini sil
        else if (elementToDelete instanceof Directory) {
            fileSystem.deleteDirectory(name, fileSystem.getCurrentDirectory());
            System.out.println("Directory deleted: " + name + "/");
        }
    }

    private static void moveFileOrDirectory() {
        // Mevcut dizini göster
        System.out.println("Current directory: " + fileSystem.getCurrentPath());

        // Taşınacak öğenin adını al
        System.out.print("Enter the name of file/directory to move: ");
        String name = scanner.nextLine().trim();

        // Hedef dizinin yolunu al
        System.out.print("Enter new directory path: ");
        String newPath = scanner.nextLine().trim();

        // Taşınacak öğeyi bul
        FileSystemElement elementToMove = fileSystem.findElementByName(name, fileSystem.getCurrentDirectory());

        // Eğer öğe bulunamazsa kullanıcıyı bilgilendir
        if (elementToMove == null) {
            System.out.println("File/directory not found: " + name);
            return;
        }

        // Hedef dizini bul
        Directory newParentDirectory = (newPath.equals("/")) ? fileSystem.getRoot() : fileSystem.findDirectoryByPath(newPath);

        // Eğer hedef dizin bulunamazsa kullanıcıyı bilgilendir
        if (newParentDirectory == null) {
            System.out.println("Target directory not found: " + newPath);
            return;
        }

        // Taşıma işlemi
        if (elementToMove != newParentDirectory) {
            // Önce eski ebeveynin çocuk listesinden öğeyi kaldır
            ((Directory)elementToMove.getParent()).removeElement(elementToMove);
            // Sonra öğenin yeni ebeveynini ayarla
            elementToMove.setParent(newParentDirectory);
            // Yeni ebeveynin çocuk listesine öğeyi ekle
            newParentDirectory.addElement(elementToMove);
            System.out.println("Moved " + name + " to " + newPath);
        } else {
            System.out.println("Cannot move to the same directory.");
        }
    }

    private static void searchFileOrDirectory() {
        System.out.print("Enter search query: ");
        String query = scanner.nextLine().trim();

        // Kök dizinden başlayarak aramayı başlat
        System.out.println("Searching from root...");
        FileSystemElement foundElement = fileSystem.getRoot().search(query);

        // Arama sonucunu kontrol et ve kullanıcıya bilgi ver
        if (foundElement != null) {
            System.out.println("Found: " + foundElement.getPath());
        } else {
            System.out.println("No file or directory found with name: " + query);
        }
    }

    private static void printDirectoryTree() {
        System.out.println("Path to current directory from root:");
        printDirectoryTree(fileSystem.getRoot(), "", true);
    }

    private static void printDirectoryTree(Directory dir, String prefix, boolean isRoot) {
        if (isRoot) {
            System.out.println("* " + dir.getName());
        } else {
            System.out.println(prefix + "* " + dir.getName());
        }
        // Recursively print the tree structure for subdirectories
        for (FileSystemElement elem : dir.getChildren()) {
            if (elem instanceof Directory) {
                // For a subdirectory, add an indentation to the prefix
                printDirectoryTree((Directory) elem, prefix + "    ", false);
            } else {
                // Files are printed directly
                System.out.println(prefix + "    * " + elem.getName());
            }
        }
    }

    private static void sortContentsByDate() {
        System.out.println("Sorting contents of the directory by date created: ");
        // Current directory'den dosya ve dizin listesini al ve tarihine göre sırala
        List<FileSystemElement> sortedList = new ArrayList<>(fileSystem.getCurrentDirectory().getChildren());
        sortedList.sort(Comparator.comparing(FileSystemElement::getDateCreated));

        // Sıralı listeyi yazdır
        for (FileSystemElement elem : sortedList) {
            String type = (elem instanceof Directory) ? "Directory" : "File";
            System.out.println(type + ": " + elem.getName() + " (Created: " + elem.getDateCreated() + ")");
        }
    }

}
