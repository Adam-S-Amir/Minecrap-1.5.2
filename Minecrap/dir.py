import os
import shutil

# Define the source and destination directories
base_dir = os.getcwd()
output_dir = os.path.join(base_dir, "_Output")

# Create a list of folders to exclude
exclude_folders = [".vscode", "_Output"]

# Create lists to store information
compiled_files = []
copied_folders = []
omitted_files = []
omitted_folders = []

# Create _Output directory
os.makedirs(output_dir, exist_ok=True)
print("Created _Output directory.")

# Copy all files (excluding .py files and .vscode folder) to _Output
for root, dirs, files in os.walk(base_dir):
    for folder in list(dirs):  # Use list() to make a copy of dirs for modification
        if folder in exclude_folders:
            dirs.remove(folder)  # Exclude the folder from further processing
            omitted_folders.append(os.path.join(root, folder))
    for file in files:
        source_path = os.path.join(root, file)
        relative_path = os.path.relpath(source_path, base_dir)
        destination_path = os.path.join(output_dir, relative_path)

        if file.endswith(".py"):
            omitted_files.append(source_path)
        else:
            os.makedirs(os.path.dirname(destination_path), exist_ok=True)
            shutil.copy2(source_path, destination_path)
            if source_path.endswith(".java"):
                compiled_files.append(source_path)
            else:
                copied_folders.append(source_path)

# Output information
print("\nFiles compiled into .class:")
for file in compiled_files:
    print(file)

print("\nFolders copied into _Output:")
for folder in copied_folders:
    print(folder)

print("\nOmitted files:")
for file in omitted_files:
    print(file)

print("\nOmitted folders:")
for folder in omitted_folders:
    print(folder)
