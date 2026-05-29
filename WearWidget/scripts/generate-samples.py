import re
import os

WIDGET_DIR = 'app/src/main/java/com/google/example/wear_widget/widget/'
OUTPUT_PATH = 'screenshots/SAMPLES.md'

def parse_file(file_path):
    with open(file_path, 'r') as f:
        lines = f.readlines()

    samples = []
    i = 0
    while i < len(lines):
        line = lines[i]
        stripped = line.strip()

        # Look for function definition
        if "fun " in stripped and "@RemoteComposable" in "".join(lines[max(0, i-5):i+1]):
            # Check if it has @RemoteComposable annotation previously
            is_remote = False
            headers = []
            j = i - 1
            while j >= 0:
                l = lines[j].strip()
                if l.startswith("@"):
                    headers.insert(0, lines[j])
                    if "@RemoteComposable" in l:
                        is_remote = True
                elif l.startswith("*/") or l.strip().startswith("*") or l.startswith("/**"):
                    headers.insert(0, lines[j])
                elif l == "" or l.endswith("}"):
                    break
                else:
                    break
                j -= 1

            if is_remote and "private" not in stripped:
                func_name_match = re.search(r"fun\s+(\w+)", stripped)
                if func_name_match:
                    func_name = func_name_match.group(1)
                    
                    # Capture code
                    code_lines = headers + [line]
                    brace_count = line.count('{') - line.count('}')
                    
                    k = i + 1
                    while k < len(lines) and (brace_count > 0 or '{' not in "".join(code_lines)):
                        l = lines[k]
                        code_lines.append(l)
                        brace_count += l.count('{')
                        brace_count -= l.count('}')
                        k += 1
                    
                    # Extract KDoc
                    kdoc_lines = [cl for cl in headers if cl.strip().startswith("/") or cl.strip().startswith("*")]
                    kdoc_text = parse_kdoc(kdoc_lines)
                    
                    samples.append({
                        'name': func_name,
                        'description': kdoc_text,
                        'code': "".join(code_lines).strip()
                    })
                    i = k - 1
        i += 1
    return samples

def parse_kdoc(lines):
    desc = []
    for line in lines:
        s = line.strip()
        if s.startswith("/**"): s = s[3:]
        if s.endswith("*/"): s = s[:-2]
        if s.startswith("*"): s = s[1:].strip()
        s = s.strip()
        if s:
            desc.append(s)
    return " ".join(desc)

def main():
    if not os.path.exists(WIDGET_DIR):
        print(f"Error: {WIDGET_DIR} not found")
        return

    all_samples = []
    for filename in sorted(os.listdir(WIDGET_DIR)):
        if filename.endswith(".kt"):
            all_samples.extend(parse_file(os.path.join(WIDGET_DIR, filename)))

    # Generate MD
    with open(OUTPUT_PATH, 'w') as f:
        f.write("# Widget Samples\n\n")
        for sample in all_samples:
            # Map function name to screenshot name if needed
            # (In this project, they mostly match the layout name)
            screenshot_name = sample['name'].replace("ReferenceSample", "Sample")
            
            f.write(f"## {sample['name']}\n\n")
            if sample['description']:
                f.write(f"{sample['description']}\n\n")
            
            f.write(f"![{sample['name']}]({screenshot_name}.png)\n\n")
            f.write("```kotlin\n")
            f.write(sample['code'])
            f.write("\n```\n\n")
            
    print(f"Generated {len(all_samples)} samples in {OUTPUT_PATH}")

if __name__ == "__main__":
    main()
