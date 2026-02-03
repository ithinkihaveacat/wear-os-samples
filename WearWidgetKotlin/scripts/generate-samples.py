import re
import os

FILE_PATH = 'app/src/main/java/com/google/example/wear_widget/WidgetCatalog.kt'
OUTPUT_PATH = 'screenshots/SAMPLES.md'

def main():
    if not os.path.exists(FILE_PATH):
        print(f"Error: {FILE_PATH} not found")
        return

    with open(FILE_PATH, 'r') as f:
        lines = f.readlines()

    samples = []
    
    i = 0
    while i < len(lines):
        line = lines[i]
        stripped = line.strip()
        
        # Look for function definition
        if stripped.startswith("fun ") or " fun " in stripped:
            # Check if it has @RemoteComposable annotation previously
            # Scan backwards
            is_remote = False
            start_idx = i
            
            # Find the start of the block (annotations + kdoc)
            # Scan back capturing headers
            headers = []
            j = i - 1
            while j >= 0:
                l = lines[j].strip()
                if l.startswith("@"):
                    headers.insert(0, lines[j])
                    if "@RemoteComposable" in l:
                        is_remote = True
                elif l.startswith("*/") or l.startswith("*") or l.startswith("/**"):
                    headers.insert(0, lines[j])
                elif l == "":
                    if headers and (headers[0].strip().startswith("@") or headers[0].strip().startswith("/*") or headers[0].strip().startswith("*")):
                         pass
                    else:
                         pass
                elif l.endswith("}"):
                    break
                else:
                    break
                j -= 1
            
            if is_remote and "private" not in stripped:
                # Capture function body
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
                    kdoc_text = ""
                    
                    kdoc_lines = [cl for cl in headers if cl.strip().startswith("/") or cl.strip().startswith("*")]
                    kdoc_text = parse_kdoc(kdoc_lines)
                    
                    samples.append({
                        'name': func_name,
                        'description': kdoc_text,
                        'code': "".join(code_lines).strip()
                    })
                    
                    i = k - 1
        i += 1

    # Generate MD
    with open(OUTPUT_PATH, 'w') as f:
        f.write("# Widget Samples\n\n")
        for sample in samples:
            f.write(f"## {sample['name']}\n\n")
            if sample['description']:
                f.write(f"{sample['description']}\n\n")
            
            f.write(f"![{sample['name']}]({sample['name']}.png)\n\n")
            f.write("```kotlin\n")
            f.write(sample['code'])
            f.write("\n```\n\n")
            
    print(f"Generated {len(samples)} samples in {OUTPUT_PATH}")

def parse_kdoc(lines):
    desc = []
    for line in lines:
        s = line.strip()
        if s.startswith("/**"): s = s[3:]
        if s.endswith("*/"): s = s[:-2]
        if s.startswith("*"): s = s[1:]
        s = s.strip()
        if s:
            desc.append(s)
    return " ".join(desc)

if __name__ == "__main__":
    main()
