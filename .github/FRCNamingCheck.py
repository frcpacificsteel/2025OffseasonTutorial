import re
import os

def is_pascal_case(name: str) -> bool:
    return re.match(r'^[A-Z][a-zA-Z0-9]*$', name) is not None

def is_camel_case(name: str) -> bool:
    return re.match(r'^[a-z][a-zA-Z0-9]*$', name) is not None and '_' not in name

def is_constant(name: str) -> bool:
    return re.match(r'^[A-Z][A-Z0-9_]*$', name) is not None

def check_java_file(filepath):
    errors = []
    with open(filepath, "r", encoding="utf-8") as f:
        lines = f.readlines()

    # Class name check
    for i, line in enumerate(lines):
        class_match = re.match(r'\s*(public\s+)?class\s+(\w+)', line)
        if class_match:
            class_name = class_match.group(2)
            if not is_pascal_case(class_name):
                errors.append(f"{filepath}:{i+1}: Class `{class_name}` should be PascalCase")

    # Constant check (public static final)
    for i, line in enumerate(lines):
        const_match = re.match(r'.*static\s+final\s+\w+\s+(\w+)', line)
        if const_match:
            const_name = const_match.group(1)
            if not is_constant(const_name):
                errors.append(f"{filepath}:{i+1}: Constant `{const_name}` should be ALL_CAPS_WITH_UNDERSCORES")

    # Variable/Instance check (simple: type name = ...;)
    for i, line in enumerate(lines):
        if 'static final' in line:
            continue
        var_match = re.match(r'\s*(private|protected|public)?\s*(final\s+)?\w+\s+(\w+)\s*(=|;)', line)
        if var_match:
            var_name = var_match.group(3)
            if not is_camel_case(var_name):
                errors.append(f"{filepath}:{i+1}: Variable `{var_name}` should be camelCase")

    # Method name check
    for i, line in enumerate(lines):
        method_match = re.match(r'\s*(public|protected|private)?\s*\w+\s+(\w+)\s*\(', line)
        if method_match:
            method_name = method_match.group(2)
            if method_name in ["if", "for", "while", "switch"]:
                continue
            if not is_camel_case(method_name):
                if not str(method_name) == str(filepath).replace(".java","").replace("src/main/java/frc/robot/","").replace("/subsystems",""):
                    errors.append(f"{filepath}:{i+1}: Method `{method_name}` should be camelCase")
    return errors

def main():
    folder = "src/main/java/frc/robot"
    all_errors = []
    java_files = []
    for root, dirs, files in os.walk(folder):
        for file in files:
            if file.endswith(".java"):
                path = os.path.join(root, file)
                java_files.append(path)
                print(f"Checking {path} ...", flush=True)
                errors = check_java_file(path)
                all_errors.extend(errors)

    # Markdown for README
    with open("violations.md", "w", encoding="utf-8") as vf:
        vf.write("# FRC Naming Convention Report\n\n")
        vf.write("## Files in src/main/java/frc/robot\n")
        for f in java_files:
            vf.write(f"- `{f}`\n")
        vf.write("\n## Naming Convention Violations\n")
        if all_errors:
            for err in all_errors:
                vf.write(f"- {err}\n")
        else:
            vf.write("No naming convention violations found.\n")

if __name__ == "__main__":
    main()
