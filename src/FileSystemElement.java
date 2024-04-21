import java.sql.Timestamp;

public abstract class FileSystemElement {
    protected String name;
    public Timestamp dateCreated;
    protected FileSystemElement parent;

    public FileSystemElement(String name, FileSystemElement parent) {
        this.name = name;
        this.parent = parent;
        this.dateCreated = new Timestamp(System.currentTimeMillis());
    }

    public String getName() {
        return name;
    }
    // Ebeveyn referansını ayarlayan metod
    public void setParent(Directory parent) {
        this.parent = parent;
    }

    public Timestamp getDateCreated() {
        return dateCreated;
    }

    public FileSystemElement getParent() {
        return parent;
    }

    // Bu soyut sınıfın diğer ortak metodları burada tanımlanabilir.
    // Örneğin, her eleman için ortak bir 'print' metodunuz varsa şu şekilde olabilir:

    public abstract void print(String prefix);

    // Bir öğenin tam yolunu döndüren metod
    public String getPath() {
        if (this.parent == null) {
            return this.name;
        } else {
            return this.parent.getPath() + "/" + this.name;
        }
    }
}
