import os
import subprocess

# Initialize an empty list to store relative file paths
file_list = []

# Get the current directory
current_dir = os.getcwd()

# Iterate through all files in the current directory and its subdirectories
for root, dirs, files in os.walk(current_dir):
    for file in files:
        if file.endswith('.java'):
            # Construct the relative file path
            relative_file_path = os.path.relpath(os.path.join(root, file), current_dir)

            # Add the relative file path to the list
            file_list.append(relative_file_path)

# Compile each Java file separately
for file_path in file_list:
    subprocess.run(["javac", file_path])

# Pause to view any error messages
input("Compilation completed. Press Enter to exit...")
