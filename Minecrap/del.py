import os

def find_and_delete_class_files(directory):
    deleted_files = []

    for root, dirs, files in os.walk(directory):
        for file in files:
            if file.endswith('.class'):
                file_path = os.path.join(root, file)
                print("Found:", file_path)
                deleted_files.append(file_path)

    if not deleted_files:
        print("No .class files found.")
        return

    confirmation = input(f"Delete {len(deleted_files)} .class file(s)? (y/n): ").strip().lower()

    if confirmation == 'y':
        for file_path in deleted_files:
            os.remove(file_path)
            print("Deleted:", file_path)
        print("Deletion completed.")
    else:
        print("Deletion canceled.")

if __name__ == "__main__":
    directory = input("Enter the directory to search for .class files: ").strip()

    if not os.path.exists(directory):
        print("Directory not found.")
    else:
        find_and_delete_class_files(directory)
