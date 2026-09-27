import os
import re

# 配置源代码目录和输出文件路径
SRC_DIR = r'src/main/java'
OUTPUT_FILE = r'copyright_code_output.txt'

# 更健壮的类定义正则，支持多种修饰符和泛型
CLASS_DEF_REGEX = re.compile(r'^\s*(public|private|protected|abstract|final|static|sealed|non-sealed|strictfp)?\s*class\s+\w+.*\{')
IMPORT_REGEX = re.compile(r'^\s*import\s+.+;')
PACKAGE_REGEX = re.compile(r'^\s*package\s+.+;')
# 匹配注释
SINGLE_LINE_COMMENT = re.compile(r'//.*')
MULTI_LINE_COMMENT = re.compile(r'/\*.*?\*/', re.DOTALL)


def remove_comments_and_blank_lines(code):
    # 移除多行注释
    code = re.sub(MULTI_LINE_COMMENT, '', code)
    # 移除单行注释
    code = re.sub(SINGLE_LINE_COMMENT, '', code)
    # 按行处理，移除空行和首尾空白
    lines = [line.rstrip() for line in code.splitlines() if line.strip()]
    return lines


def extract_package_imports_classes(filepath):
    with open(filepath, 'r', encoding='utf-8') as f:
        code = f.read()
    lines = code.splitlines()
    package_line = ''
    imports = []
    for line in lines:
        if PACKAGE_REGEX.match(line):
            package_line = line.strip()
        elif IMPORT_REGEX.match(line):
            imports.append(line.strip())
    classes = []
    n = len(lines)
    i = 0
    while i < n:
        if CLASS_DEF_REGEX.match(lines[i]):
            brace_count = 0
            class_start = i
            found_open = False
            # 找到第一个 { 作为类体开始
            for j in range(i, n):
                brace_count += lines[j].count('{')
                brace_count -= lines[j].count('}')
                if '{' in lines[j]:
                    found_open = True
                if found_open and brace_count == 0:
                    class_end = j
                    class_code = '\n'.join(lines[class_start:class_end+1])
                    classes.append(class_code)
                    i = class_end
                    break
            else:
                i += 1
                continue
        i += 1
    return package_line, imports, classes


def main():
    file_blocks = []
    for root, _, files in os.walk(SRC_DIR):
        for file in files:
            if file.endswith('.java'):
                filepath = os.path.join(root, file)
                package_line, imports, classes = extract_package_imports_classes(filepath)
                class_blocks = []
                for class_code in classes:
                    lines = remove_comments_and_blank_lines(class_code)
                    if len(lines) >= 5:
                        class_blocks.append('\n'.join(lines))
                if class_blocks:
                    block = ''
                    # 如果有package语句，先写入package语句
                    if package_line:
                        block += package_line + '\n'
                    # 如果有import语句，写入import语句
                    if imports:
                        block += '\n'.join(imports) + '\n'
                    block += ''.join(class_blocks)
                    file_blocks.append(block)
    # 文件之间用一个换行分隔，绝无空行
    with open(OUTPUT_FILE, 'w', encoding='utf-8') as out:
        out.write('\n'.join(file_blocks))
    print(f'输出完成，共输出 {len(file_blocks)} 个文件，文件：{OUTPUT_FILE}')


if __name__ == '__main__':
    main()
