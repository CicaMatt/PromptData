import pandas as pd
from sklearn import preprocessing

# Sample DataFrame (you can replace this with the actual one)
df = pd.DataFrame({
    'pets': ['cat', 'dog', 'cat', 'monkey', 'dog', 'dog'], 
    'owner': ['Champ', 'Ron', 'Brick', 'Champ', 'Veronica', 'Ron'], 
    'location': ['San_Diego', 'New_York', 'New_York', 'San_Diego', 'San_Diego', 'New_York']
})

# Initialize a dictionary to store label encoders for each column
label_encoders = {}

# Loop through each column in the DataFrame
for col in df.columns:
    if df[col].dtype == 'object':  # Apply label encoding only to columns with string data
        le = preprocessing.LabelEncoder()  # Create a LabelEncoder instance
        df[col] = le.fit_transform(df[col])  # Fit and transform the column data
        label_encoders[col] = le  # Store the LabelEncoder for future use or inverse transform

# Display the transformed DataFrame
print("Transformed DataFrame:")
print(df)

# Example: Demonstrating how to reverse the encoding for a specific column (e.g., 'pets')
original_pets_labels = label_encoders['pets'].inverse_transform(df['pets'])
print("\nOriginal 'pets' labels restored:")
print(original_pets_labels)
