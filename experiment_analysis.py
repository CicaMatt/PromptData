from collections import Counter

import pandas as pd
from scipy.stats import shapiro, wilcoxon


def calculate_ratios_per_question(file_path, output_path):
    # Load the original dataset
    df = pd.read_csv(file_path)

    # List of LLMs and prompts
    llms = ['gpt', 'gemini', 'copilot', 'codellama']
    prompts = ['s', 'm', 'h']

    # Empty DataFrame to contain only the new columns
    new_columns_df = pd.DataFrame()

    # Loop for each LLM and each prompt level
    for llm in llms:
        for prompt in prompts:
            # Column names for cwe_count, cq, and loc
            cwe_col = f"{llm}_cwe_count_{prompt}"
            cq_col = f"{llm}_cq_{prompt}"
            loc_col = f"{llm}_loc_{prompt}"

            # Check if the columns exist in the DataFrame
            if all(col in df.columns for col in [cwe_col, cq_col, loc_col]):
                # Convert columns to numeric, handle NaN
                df[cwe_col] = pd.to_numeric(df[cwe_col], errors='coerce').fillna(0)
                df[cq_col] = pd.to_numeric(df[cq_col], errors='coerce').fillna(0)
                df[loc_col] = pd.to_numeric(df[loc_col], errors='coerce').fillna(0)

                # Calculate new ratios, handle division by zero
                cwe_loc_ratio = df[cwe_col] / df[loc_col].replace({0: pd.NA})
                cq_loc_ratio = df[cq_col] / df[loc_col].replace({0: pd.NA})

                # Add new columns to the DataFrame of new columns
                new_columns_df[f"{llm}_cwe_loc_{prompt}"] = cwe_loc_ratio
                new_columns_df[f"{llm}_cq_loc_{prompt}"] = cq_loc_ratio

    # Save the DataFrame with only the new columns to a new CSV file
    new_columns_df.to_csv(output_path, index=False)


def split_csv_by_column_names(input_filename, cwe_output, cq_output):
    # Read the CSV file
    df = pd.read_csv(input_filename)
    # Select the columns that contain 'cwe' in the name
    cwe_columns = [col for col in df.columns if 'cwe' in col.lower()]
    # Select the columns that contain 'cq' in the name
    cq_columns = [col for col in df.columns if 'cq' in col.lower()]
    # Create DataFrame with 'cwe' columns
    df_cwe = df[cwe_columns]
    # Create DataFrame with 'cq' columns
    df_cq = df[cq_columns]
    # Save the DataFrames to new CSV files
    df_cwe.to_csv(cwe_output, index=False)
    df_cq.to_csv(cq_output, index=False)


def calculate_mean(input_file, output_file):
    # Read the CSV file into a DataFrame
    df = pd.read_csv(input_file)

    # Calculate the mean of each column
    means = df.mean()

    # Create a DataFrame with the means
    df_medie = pd.DataFrame(means, columns=['Mean'])

    # Save the DataFrame of means to a new CSV file
    df_medie.to_csv(output_file)


def count_all_cwe_ids(file_path):
    # Read the CSV file
    df = pd.read_csv(file_path)

    # Filter columns that contain "cwe_id"
    cwe_columns = [col for col in df.columns if 'cwe_id' in col]

    # For each column, count occurrences of cwe_id
    for col in cwe_columns:
        # Join all rows of the column into a list of identifiers
        cwe_list = []
        for value in df[col].dropna():
            # Split space-separated cwe_id and add to the list, excluding "0"
            cwe_list.extend([cwe for cwe in value.split() if cwe != '0'])

        # Count occurrences of cwe_id
        cwe_counter = Counter(cwe_list)

        # Sort by occurrences in descending order
        sorted_cwe = sorted(cwe_counter.items(), key=lambda x: x[1], reverse=True)
        ordered_cwe = sorted([int(cwe) for cwe, _ in cwe_counter.items()])
        cwe_types_number = len(cwe_counter.items())

        # Print the result for the current column
        print(f"Column: {col}")
        for cwe, count in sorted_cwe:
            print(f"{cwe}: {count}")

        # Calculate and print the total CWE found in the current column
        total_cwe = sum(cwe_counter.values())
        print(f"Total CWE per column {col}: {total_cwe}")
        print("CWE ordered by ID: " + str(ordered_cwe))
        print("Total CWE types: " + str(cwe_types_number))
        print("-" * 40)


def most_frequent_cwe(file_path, considered_columns):
    # Read the CSV file
    df = pd.read_csv(file_path)

    # Filtering by language
    #df = df[df['language'].str.contains('PHP', na=False, case=False)]

    # Filter columns by considered columns
    cwe_columns = [col for col in df.columns if considered_columns in col]
    # List to contain all relevant columns
    all_cwe_list = []

    # For each column, add cwe_id to the general list excluding "0"
    for col in cwe_columns:
        for value in df[col].dropna():
            # Split space-separated cwe_id and add to the general list, excluding "0"
            all_cwe_list.extend([cwe for cwe in value.split() if cwe != '0'])

    # Count occurrences of each cwe_id
    cwe_counter = Counter(all_cwe_list)

    # Sort by occurrences in descending order
    sorted_cwe = sorted(cwe_counter.items(), key=lambda x: x[1], reverse=True)

    # Calculate the general frequency as a ratio of occurrences to total
    total_occurrences = sum(cwe_counter.values())

    print("Ordered list of the most frequent CWE:")
    for cwe, count in sorted_cwe:
        frequency = (count / total_occurrences) * 100  # Calculate the frequency percentage
        print(f"{cwe}: {count} occurrences, {frequency:.2f}% general frequency")
    print("-" * 40)

    top_5_cwe = [f"CWE-{cwe}" for cwe, count in sorted_cwe[:5]]
    print("Top 5 CWE:", ", ".join(top_5_cwe))


def count_cq_per_column(file_path):
    df = pd.read_csv(file_path)

    # Filter columns that contain "cq" in the name
    cq_columns = [col for col in df.columns if 'cq' in col.lower()]

    # Create a dictionary to store the sum of values for each column
    sums = {}

    # Calculate the sum for each column and store it in the dictionary
    for col in cq_columns:
        sums[col] = df[col].sum()

    return sums


def check_normality_per_column(file_path):
    try:
        # Load the CSV file into a DataFrame
        dataframe = pd.read_csv(file_path)

        # Filter columns that contain 'cwe_count', 'cq', or 'loc' in the name
        considered_columns = [col for col in dataframe.columns if
                               any(keyword in col for keyword in ['cwe_count', 'cq', 'loc'])]

        result = {}

        # For each column, perform the Shapiro-Wilk test
        for column in considered_columns:
            try:
                # Convert the column to numeric values, replacing non-convertible values with NaN
                dataframe[column] = pd.to_numeric(dataframe[column], errors='coerce')

                # Perform the test only if there are at least 3 numeric values
                if dataframe[column].count() >= 3:
                    stat, p_value = shapiro(dataframe[column].dropna())
                    # Check if the p-value is greater than 0.05
                    result[column] = 'Normal' if p_value > 0.05 else 'Non normal'
                else:
                    result[column] = 'Unable to process'
            except Exception as e:
                result[column] = f"Processing error: {e}"

        return result

    except FileNotFoundError:
        return "Error: file not found."
    except pd.errors.EmptyDataError:
        return "Error: empty file or unvalid data."
    except Exception as e:
        return f"Error: {e}"


def wilcoxon_test_ratio(file_path, field, alpha=0.05):
    # Load the dataset from the CSV
    df = pd.read_csv(file_path)

    # List of LLMs and the field of interest
    llms = ['gpt', 'gemini', 'copilot', 'codellama']
    prompts = ['s', 'm', 'h']

    # Dictionary to store the results of the Wilcoxon tests
    results = {}

    # Loop over each LLM
    for llm in llms:
        # Build the column names for the three prompts (s, m, h) for the current LLM
        columns = {prompt: f"{llm}_{field}_{prompt}" for prompt in prompts}

        # Check if the columns exist in the DataFrame
        if all(col in df.columns for col in columns.values()):
            # Create a key for the results of this LLM
            results[llm] = {}

            # Convert the columns to numeric, handle errors
            try:
                for col in columns.values():
                    df[col] = pd.to_numeric(df[col], errors='coerce')

                # Remove any NaN values generated by the conversion
                valid_data = df[list(columns.values())].dropna()

                # Perform the Wilcoxon test between the various combinations of prompts
                w_s_m = wilcoxon(valid_data[columns['s']], valid_data[columns['m']])
                w_s_h = wilcoxon(valid_data[columns['s']], valid_data[columns['h']])
                w_m_h = wilcoxon(valid_data[columns['m']], valid_data[columns['h']])

                # Save the results in the dictionary, adding 'statistical_difference'
                results[llm]['s_m'] = {
                    'statistic': w_s_m.statistic,
                    'p_value': w_s_m.pvalue,
                    'statistical_difference': w_s_m.pvalue < alpha
                }
                results[llm]['s_h'] = {
                    'statistic': w_s_h.statistic,
                    'p_value': w_s_h.pvalue,
                    'statistical_difference': w_s_h.pvalue < alpha
                }
                results[llm]['m_h'] = {
                    'statistic': w_m_h.statistic,
                    'p_value': w_m_h.pvalue,
                    'statistical_difference': w_m_h.pvalue < alpha
                }
            except Exception as e:
                # Handle any errors during the conversion or statistical test
                results[llm]['error'] = str(e)

    return results