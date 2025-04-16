from bs4 import BeautifulSoup
import csv
import sys


def parse_questions(xhtml):
    soup = BeautifulSoup(xhtml, 'lxml')
    questions = []

    i = 1

    for li in soup.find('ol').children:
        if li == '\n':
            continue

        title_elements = []

        for child in li.contents:
            if child == '\n':
                continue
            if child.name == "ol":
                break
            title_elements.append(child)

        # Combine the title elements together as a string
        title_elements_str = ""

        for el in title_elements:
            title_elements_str += str(el)

        options = []
        correct = -1
        for option in li.find_next('ol').children:
            if option == '\n':
                continue
            if 'correct' in option.get('class', []):
                correct = len(options)
            options.append(option)

        difficulty = str(li.find_all_next('dd')[2])

        if difficulty == '<dd>Easy</dd>' or difficulty == '<dd>easy</dd>':
            difficulty = 1
        elif difficulty == '<dd>Medium</dd>':
            difficulty = 2
        elif difficulty == '<dd>Hard</dd>':
            difficulty = 3

        formatted_question = {
            "question_num": i,
            "good_question": 1,  # Default set to true -> can manually change
            "difficulty": difficulty,
            "title": title_elements_str,
            "option_a": str(options[0]),
            "option_b": str(options[1]),
            "option_c": str(options[2]),
            "option_d": str(options[3]),
            "correct": correct + 1
        }

        questions.append(formatted_question)

        i += 1

    return questions


def write_questions_to_csv(file_name, questions):
    headers = ['question_num', 'good_question', 'difficulty', 'title',
               'option_a', 'option_b', 'option_c', 'option_d', 'correct']

    with open(f'{file_name}.csv', mode='w', newline='', encoding='utf-8') as file:
        writer = csv.DictWriter(file, fieldnames=headers)

        writer.writeheader()

        for q in questions:
            writer.writerow(q)


if __name__ == "__main__":
    # takes file where xhtml is stored as the second argument and saves to csv with same filename
    file_name = sys.argv[1].split('.')[0]
    with open(f"{file_name}.xhtml", "r", encoding="utf-8") as file:
        xhtml_content = file.read()

    parsed_questions = parse_questions(xhtml_content)
    write_questions_to_csv(file_name, parsed_questions)
