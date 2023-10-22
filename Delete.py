import os
import shutil

def delete_files_with_extension(directory, extension):
    for root, _, files in os.walk(directory):
        for file in files:
            if file.endswith(extension):
                file_path = os.path.join(root, file)
                print(f"Deleting: {file_path}")
                os.remove(file_path)

def main():
    directory = input("Enter the directory path: ")
    extension = input("Enter the file extension (e.g., .txt): ")

    if os.path.exists(directory):
        confirm = input(f"Are you sure you want to delete all '{extension}' files in '{directory}' and its subdirectories? (yes/no): ").lower()
        if confirm == "yes":
            delete_files_with_extension(directory, extension)
            print(f"All '{extension}' files deleted.")
        else:
            print("Deletion canceled.")
    else:
        print("Directory not found.")

if __name__ == "__main__":
    main()
